package com.gg.accountservice.modules.account.service;

import static com.gg.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample2;
import static com.gg.accountservice.modules.account.util.AccountTestUtil.createResendAuthFormWithEmailSample2;
import static com.gg.accountservice.modules.account.util.AccountTestUtil.emailSample2;
import static com.gg.accountservice.modules.account.util.AccountTestUtil.emailSample2Password;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.gg.accountservice.infra.annotation.ServiceTest;
import com.gg.accountservice.infra.container.AbstractContainerBaseTest;
import com.gg.commonservice.dto.account.AccountDto;
import com.gg.commonservice.security.CredentialInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 이메일 인증 성공 후, 로그인 테스트
 */
@ServiceTest
class AccountLoginServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private AccountService accountService;

    public void successAuthAccount() {
        accountService.saveAccount(createAccountSaveFormWithEmailSample2());
        accountService.validAuthCode(createResendAuthFormWithEmailSample2());
    }

    @Test
    @DisplayName("로그인 성공 확인")
    void successLogin() {
        // given
        successAuthAccount();
        CredentialInfo credentialInfo = new CredentialInfo(emailSample2Password);
        // when
        AccountDto loginAccount = accountService.login(emailSample2, credentialInfo);
        // then
        assertEquals(1, loginAccount.getLoginCount());
        assertNotNull(loginAccount.getRefreshToken());
    }
}
