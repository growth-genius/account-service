package com.gg.tgather.accountservice.modules.account.service;

import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample1;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample2;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.emailSample1;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.nickname;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gg.tgather.accountservice.infra.annotation.ServiceTestNoRollback;
import com.gg.tgather.accountservice.infra.container.AbstractContainerBaseTest;
import com.gg.tgather.accountservice.modules.account.entity.Account;
import com.gg.tgather.accountservice.modules.account.enums.AccountStatus;
import com.gg.tgather.accountservice.modules.account.form.AuthCodeForm;
import com.gg.tgather.accountservice.modules.account.repository.AccountRepository;
import com.gg.tgather.commonservice.advice.exceptions.RequiredAuthAccountException;
import com.gg.tgather.commonservice.dto.account.AccountDto;
import com.gg.tgather.commonservice.enums.LoginType;
import com.gg.tgather.commonservice.security.CredentialInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

@ServiceTestNoRollback
class AccountServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;


    public Account saveAccount() {

        return accountRepository.save(Account.createAccount(createAccountSaveFormWithEmailSample2()));
    }

    @Test
    @Order(0)
    @DisplayName("이메일 중복 여부 확인")
    void validEmail() {
        // given
        saveAccount();
        // when
        boolean validNickname = accountService.validNickname(nickname);
        // then
        assertTrue(validNickname);
    }

    @Test
    @Order(1)
    @DisplayName("회원가입 확인")
    void test_case_1() {

        AccountDto accountDto = accountService.saveAccount(createAccountSaveFormWithEmailSample1());
        assertEquals(emailSample1, accountDto.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("인증코드 미인증 시 예외 발생")
    void test_case_2() {
        CredentialInfo credentialInfo = new CredentialInfo("TestYeji0529!", LoginType.TGAHTER);
        RequiredAuthAccountException requiredAuthAccountException = assertThrows(RequiredAuthAccountException.class,
            () -> accountService.login(emailSample1, credentialInfo));
        assertEquals("이메일에 전송된 인증코드를 확인해주세요.", requiredAuthAccountException.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("이메일 인증코드 오기입시 예외 발생")
    void test_case_4() {
        //given
        AuthCodeForm authCodeForm = new AuthCodeForm();
        authCodeForm.setAuthCode("test_");
        authCodeForm.setEmail(emailSample1);
        //when
        BadCredentialsException badCredentialsException = assertThrows(BadCredentialsException.class, () -> accountService.validAuthCode(authCodeForm));
        //then
        assertEquals("인증 코드가 잘못되었습니다. 다시 확인해주세요.", badCredentialsException.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("올바른 이메일 인증코드 기입시 인증 성공")
    void test_case_3() {
        //given
        AuthCodeForm authCodeForm = new AuthCodeForm();
        authCodeForm.setAuthCode("authcode_");
        authCodeForm.setEmail(emailSample1);
        //when
        accountService.validAuthCode(authCodeForm);
        Account account = accountRepository.findByEmail(emailSample1).orElseThrow();
        //then
        assertEquals(AccountStatus.NORMAL, account.getAccountStatus());
    }

}