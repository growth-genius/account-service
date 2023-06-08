package com.gg.tgather.accountservice.modules.account.service;

import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.EMAIL_SAMPLE_1;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gg.tgather.accountservice.infra.annotation.EnableTestcontainers;
import com.gg.tgather.accountservice.infra.annotation.ServiceTest;
import com.gg.tgather.accountservice.modules.account.entity.Account;
import com.gg.tgather.accountservice.modules.account.enums.AccountStatus;
import com.gg.tgather.accountservice.modules.account.form.AuthCodeForm;
import com.gg.tgather.accountservice.modules.account.repository.AccountRepository;
import com.gg.tgather.commonservice.advice.exceptions.RequiredAuthAccountException;
import com.gg.tgather.commonservice.dto.account.AccountDto;
import com.gg.tgather.commonservice.enums.LoginType;
import com.gg.tgather.commonservice.security.CredentialInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * 이메일 인증 관련 테스트코드 작성
 */
@ServiceTest
@EnableTestcontainers
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9093", "port=9093"})
class AccountAuthServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void init() {
        AccountDto accountDto = accountService.saveAccount(createAccountSaveFormWithEmailSample1());
        assertEquals(EMAIL_SAMPLE_1, accountDto.getEmail());
    }


    @Test
    @DisplayName("인증코드 미인증 시 예외 발생")
    void whenAuthCodeNotValidation_thenExceptionThrows() {
        CredentialInfo credentialInfo = new CredentialInfo("TestYeji0529!", LoginType.TGAHTER);
        RequiredAuthAccountException requiredAuthAccountException = assertThrows(RequiredAuthAccountException.class,
            () -> accountService.login(EMAIL_SAMPLE_1, credentialInfo));
        assertEquals("이메일에 전송된 인증코드를 확인해주세요.", requiredAuthAccountException.getMessage());
    }

    @Test
    @DisplayName("이메일 인증코드 오기입시 예외 발생")
    void whenAuthCodeWrong_thenExceptionThrows() {
        //given
        AuthCodeForm authCodeForm = new AuthCodeForm();
        authCodeForm.setAuthCode("test_");
        authCodeForm.setEmail(EMAIL_SAMPLE_1);
        //when
        BadCredentialsException badCredentialsException = assertThrows(BadCredentialsException.class, () -> accountService.validAuthCode(authCodeForm));
        //then
        assertEquals("인증 코드가 잘못되었습니다. 다시 확인해주세요.", badCredentialsException.getMessage());
    }

    @Test
    @DisplayName("올바른 이메일 인증코드 기입시 인증 성공")
    void whenAuthCodeCorrect_thenAuthenticationSuccess() {
        //given
        AuthCodeForm authCodeForm = new AuthCodeForm();
        authCodeForm.setAuthCode("authcode_");
        authCodeForm.setEmail(EMAIL_SAMPLE_1);
        //when
        accountService.validAuthCode(authCodeForm);
        Account account = accountRepository.findByEmail(EMAIL_SAMPLE_1).orElseThrow();
        //then
        assertEquals(AccountStatus.NORMAL, account.getAccountStatus());
    }

}
