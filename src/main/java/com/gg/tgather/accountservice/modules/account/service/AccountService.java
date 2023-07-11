package com.gg.tgather.accountservice.modules.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gg.tgather.accountservice.modules.account.dto.CustomAccountDto;
import com.gg.tgather.accountservice.modules.account.dto.TokenDto;
import com.gg.tgather.accountservice.modules.account.entity.Account;
import com.gg.tgather.accountservice.modules.account.enums.AccountStatus;
import com.gg.tgather.accountservice.modules.account.form.AccountSaveForm;
import com.gg.tgather.accountservice.modules.account.form.AuthCodeForm;
import com.gg.tgather.accountservice.modules.account.form.AutoSignInForm;
import com.gg.tgather.accountservice.modules.account.form.ModifyAccountForm;
import com.gg.tgather.accountservice.modules.account.form.RenewalRefreshToken;
import com.gg.tgather.accountservice.modules.account.form.ResendAuthForm;
import com.gg.tgather.accountservice.modules.account.form.SignInForm;
import com.gg.tgather.accountservice.modules.account.repository.AccountRepository;
import com.gg.tgather.accountservice.modules.account.service.kafka.KafkaEmailProducer;
import com.gg.tgather.commonservice.advice.exceptions.ExpiredTokenException;
import com.gg.tgather.commonservice.advice.exceptions.NoMemberException;
import com.gg.tgather.commonservice.advice.exceptions.NotFoundException;
import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.commonservice.advice.exceptions.RequiredAuthAccountException;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.commonservice.dto.account.AccountDto;
import com.gg.tgather.commonservice.dto.mail.EmailMessage;
import com.gg.tgather.commonservice.dto.mail.MailSubject;
import com.gg.tgather.commonservice.enums.LoginType;
import com.gg.tgather.commonservice.properties.KafkaUserTopicProperties;
import com.gg.tgather.commonservice.security.CredentialInfo;
import com.gg.tgather.commonservice.security.Jwt;
import com.gg.tgather.commonservice.security.Jwt.Claims;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * Account CRUD 프로세스
 *
 * @author joyeji
 * @since 2023.06.05
 */
@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class AccountService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final Jwt jwt;
    private final KafkaEmailProducer kafkaEmailProducer;
    private final KafkaUserTopicProperties kafkaUserTopicProperties;
    private final AuthService authService;

    /**
     * 회원가입
     *
     * @param accountSaveForm account 저장 폼
     * @return AccountDto account 생성 결과 Dto
     */
    public AccountDto saveAccount(AccountSaveForm accountSaveForm) {
        accountSaveForm.setPassword(passwordEncoder.encode(accountSaveForm.getPassword()));

        validateAccount(accountSaveForm);
        Account account = Account.createAccount(accountSaveForm);
        String authCode = sendSignUpConfirmEmail(account.getEmail(), account.getAccountId());
        account.updateAuthCode(authCode);
        accountRepository.save(account);
        return CustomAccountDto.from(account);
    }

    /**
     * 이메일 인증코드 전송
     *
     * @param email 이메일
     * @return authCode 인증코드
     */
    private String sendSignUpConfirmEmail(String email, String accountId) {
        String authCode = authService.createAuthCode();

        try {
            kafkaEmailProducer.send(kafkaUserTopicProperties.getAuthenticationMailTopic(),
                EmailMessage.builder().accountId(accountId).to(email).message(authCode).mailSubject(MailSubject.VALID_AUTHENTICATION_ACCOUNT).build());
        } catch (JsonProcessingException e) {
            log.error("Fail to Send Email");
        }

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
     * @param signInForm 로그인 폼
     * @return AccountDto 계정 Dto
     */
    public AccountDto login(SignInForm signInForm) {
        String email = signInForm.getEmail();
        CredentialInfo credential = new CredentialInfo(signInForm.getPassword());
        Account account = accountRepository.findByEmailAndLoginType(email, credential.getLoginType()).orElseThrow(NoMemberException::new);

        if (account.getAccountStatus() == AccountStatus.VERIFY_EMAIL) {
            throw new RequiredAuthAccountException("이메일에 전송된 인증코드를 확인해주세요.");
        }

        account.login(passwordEncoder, credential.getCredential());
        account.afterLoginSuccess(signInForm.getFcmToken());
        return CustomAccountDto.createByAccountAndGenerateAccessToken(account, jwt);
    }

    /**
     * 이메일 인증 확인
     *
     * @param authCodeForm 인증 코드 확인 form
     * @return AccountDto 인증 확인 AccountDto
     */
    public AccountDto validAuthCode(AuthCodeForm authCodeForm) {
        Account account = accountRepository.findByEmailAndLoginType(authCodeForm.getEmail(), LoginType.TGAHTER).orElseThrow(NoMemberException::new);

        if (!account.getAuthCode().equals(authCodeForm.getAuthCode())) {
            throw new BadCredentialsException("인증 코드가 잘못되었습니다. 다시 확인해주세요.");
        }
        account.successAuthUser();
        return CustomAccountDto.createByAccountAndGenerateAccessToken(account, jwt);
    }

    /**
     * 토큰 갱신
     *
     * @param renewalRefreshToken 토큰 갱신 Dto
     * @return TokenDto 갱신 토큰 결과 Dto
     */
    public TokenDto renewalTokenByRefreshToken(RenewalRefreshToken renewalRefreshToken) {
        if (jwt.validateToken(renewalRefreshToken.getRefreshToken())) {
            Claims claims = jwt.verify(renewalRefreshToken.getRefreshToken());
            Account account = accountRepository.findByEmailAndLoginType(claims.getEmail(), LoginType.TGAHTER)
                .orElseThrow(() -> new NotFoundException("이메일을 찾을 수 없습니다."));
            AccountDto accountDto = CustomAccountDto.createByAccountAndGenerateAccessToken(account, jwt);
            return TokenDto.builder().accessToken(accountDto.getAccessToken()).refreshToken(accountDto.getRefreshToken()).build();
        }
        throw new ExpiredTokenException();
    }

    public AccountDto getAccount(String accountId) {
        return CustomAccountDto.from(accountRepository.findByAccountId(accountId).orElseThrow(NoMemberException::new));
    }

    /**
     * 메일 발송 실패시 삭제
     *
     * @param emailMessage 이메일 메시지 객체
     */
    @Transactional
    public void sendEmailFail(EmailMessage emailMessage) {
        String accountId = emailMessage.getAccountId();
        Account account = accountRepository.findByAccountId(accountId).orElseThrow(NoMemberException::new);
        accountRepository.delete(account);
    }

    /**
     * 인증코드 재발송
     *
     * @param resendAuthForm 인증 코드 재발송 Form
     * @return CustomAccountDto
     */
    public Boolean resendAuthCode(ResendAuthForm resendAuthForm) {
        Account account = accountRepository.findByEmail(resendAuthForm.getEmail()).orElseThrow(NoMemberException::new);
        String authCode = sendSignUpConfirmEmail(resendAuthForm.getEmail(), resendAuthForm.getAccountId());
        account.updateAuthCode(authCode);
        return true;
    }

    /**
     * 사용자 정보 수정
     *
     * @param accountId         계정고유아이디
     * @param modifyAccountForm 계정수정폼
     * @return customAccountDto 수정된 accountDto
     */
    public CustomAccountDto modifyAccount(String accountId, ModifyAccountForm modifyAccountForm) {
        Account account = accountRepository.findByAccountId(accountId).orElseThrow(NoMemberException::new);
        account.modifyAccountInfo(modifyAccountForm);
        return CustomAccountDto.from(account);
    }

    /**
     * 이메일 유효성 여부 확인
     *
     * @param email 이메일 인증 폼
     * @return boolean 이메일 유효성 결과
     */
    public Boolean validEmailAddress(String email) {
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new OmittedRequireFieldException("이메일 형식이 올바르지 않습니다.");
        }

        return accountRepository.findByEmail(email).isPresent();
    }

    public CustomAccountDto autoLogIn(AutoSignInForm autoSignInForm) {
        String refreshToken = autoSignInForm.getRefreshToken();
        Claims claims = jwt.verify(refreshToken);
        String accountId = claims.getAccountId();
        Account account = accountRepository.findByAccountId(accountId).orElseThrow(NoMemberException::new);
        account.changeFcmTokenIfChanged(autoSignInForm.getFcmToken());

        return CustomAccountDto.createByAccountAndGenerateAccessToken(account, jwt);
    }

    public List<CustomAccountDto> getAccounts(List<String> accountIds) {
        return accountRepository.findAllByAccountIds(accountIds);
    }

    /**
     * 로그인 사용자의 정보 가져오는 메서드
     * 비밀번호를 내리지 않기 위해 CustomAccountDto 를 사용한다.
     *
     * @param authentication 로그인 사용자
     * @return CustomAccountDto 로그인 사용자 정보
     * @see CustomAccountDto#from(Account)
     */
    public CustomAccountDto getByAccount(JwtAuthentication authentication) {
        log.error("authentication.id :: {}", authentication.accountId());
        return CustomAccountDto.from(accountRepository.findByAccountId(authentication.accountId()).orElseThrow(NoMemberException::new));
    }

}
