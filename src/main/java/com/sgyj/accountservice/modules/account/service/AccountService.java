package com.sgyj.accountservice.modules.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sgyj.accountservice.infra.advice.exceptions.ExpiredTokenException;
import com.sgyj.accountservice.infra.advice.exceptions.NotFoundException;
import com.sgyj.accountservice.infra.properties.KafkaUserTopicProperties;
import com.sgyj.accountservice.infra.security.CredentialInfo;
import com.sgyj.accountservice.infra.security.Jwt;
import com.sgyj.accountservice.infra.security.Jwt.Claims;
import com.sgyj.accountservice.modules.account.dto.AccountDto;
import com.sgyj.accountservice.modules.account.dto.TokenDto;
import com.sgyj.accountservice.modules.account.dto.kafka.EmailMessage;
import com.sgyj.accountservice.modules.account.entity.Account;
import com.sgyj.accountservice.modules.account.enums.LoginType;
import com.sgyj.accountservice.modules.account.form.AccountSaveForm;
import com.sgyj.accountservice.modules.account.form.AuthCodeForm;
import com.sgyj.accountservice.modules.account.repository.AccountRepository;
import com.sgyj.accountservice.modules.account.service.kafka.KafkaEmailProducer;
import com.sgyj.accountservice.modules.common.annotation.BaseServiceAnnotation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@BaseServiceAnnotation
@RequiredArgsConstructor
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final Jwt jwt;
    private final KafkaEmailProducer kafkaEmailProducer;
    private final KafkaUserTopicProperties kafkaUserTopicProperties;

    /**
     * 2
     * 회원가입
     *
     * @param accountSaveForm account 저장 폼
     * @return AccountDto account 생성 결과 Dto
     */
    public AccountDto saveAccount(AccountSaveForm accountSaveForm) throws JsonProcessingException {
        accountSaveForm.setPassword(passwordEncoder.encode(accountSaveForm.getPassword()));

        validateAccount(accountSaveForm);

        String authCode = sendSignUpConfirmEmail(accountSaveForm.getEmail());
        Account account = Account.createAccountByFormAndAuthCode(accountSaveForm, authCode);
        accountRepository.save(account);
        // kafkaAccountProducer.send(kafkaUserTopicProperties.getAccountTopic(), accountDto);
        return AccountDto.from(account);
    }

    /**
     * 이메일 인증코드 전송
     *
     * @param email 이메일
     * @return authCode 인증코드
     */
    private String sendSignUpConfirmEmail(String email) throws JsonProcessingException {
        String authCode = RandomStringUtils.randomAlphanumeric(12);
        kafkaEmailProducer.send(kafkaUserTopicProperties.getAuthenticationMailTopic(), EmailMessage.of(email, "제목", authCode));
        return authCode;
    }

    /**
     * 입력된 정보 확인
     *
     * @param accountSaveForm account 저장 폼
     */
    private void validateAccount(AccountSaveForm accountSaveForm) {

        if (validNickname(accountSaveForm.getNickname())) {
            throw new BadCredentialsException("이미 존재하는 닉네임입니다.");
        }

        accountRepository.findByEmail(accountSaveForm.getEmail()).ifPresent(account -> {
            throw new BadCredentialsException("이미 존재하는 이메일입니다.");
        });

    }

    /**
     * nickName 중복확인
     *
     * @param nickName 닉네임
     * @return boolean 닉네임 유효값 확인 결과
     */
    public boolean validNickname(String nickName) {
        return accountRepository.findByNickname(nickName).isPresent();
    }

    /**
     * 사용자 로그인
     *
     * @param email      이메일
     * @param credential 인증
     * @return AccountDto 계정 Dto
     */
    public AccountDto login(String email, CredentialInfo credential) {
        Account account = accountRepository.findByEmailAndLoginType(email, credential.getLoginType()).orElseThrow(() -> new NotFoundException("등록된 계정이 없습니다."));
        account.login(passwordEncoder, credential.getCredential());
        account.afterLoginSuccess();
        return AccountDto.createByAccountAndGenerateAccessToken(account, jwt);
    }

    /**
     * 이메일 인증 확인
     *
     * @param authCodeForm 인증 코드 확인 form
     * @return AccountDto 인증 확인 AccountDto
     */
    public AccountDto validAuthCode(AuthCodeForm authCodeForm) {
        Account account = accountRepository.findByEmailAndLoginType(authCodeForm.getEmail(), LoginType.TGAHTER)
            .orElseThrow(() -> new NotFoundException("등록된 계정이 없습니다."));

        if (!account.getAuthCode().equals(authCodeForm.getAuthCode())) {
            throw new BadCredentialsException("인증 코드가 잘못되었습니다. 다시 확인해주세요.");
        }
        account.successAuthUser();
        return AccountDto.createByAccountAndGenerateAccessToken(account, jwt);
    }

    /**
     * 토큰 갱신
     *
     * @param tokenDto 토큰 갱신 Dto
     * @return TokenDto 갱신 토큰 결과 Dto
     */
    public TokenDto renewalTokenByRefreshToken(TokenDto tokenDto) {
        if (jwt.validateToken(tokenDto.getRefreshToken())) {
            Claims claims = jwt.verify(tokenDto.getRefreshToken());
            Account account = accountRepository.findByEmailAndLoginType(claims.getEmail(), LoginType.TGAHTER)
                .orElseThrow(() -> new NotFoundException("이메일을 찾을 수 없습니다."));
            AccountDto accountDto = AccountDto.createByAccountAndGenerateAccessToken(account, jwt);
            return TokenDto.builder().accessToken(accountDto.getAccessToken()).refreshToken(accountDto.getRefreshToken()).build();
        }
        throw new ExpiredTokenException();
    }

}
