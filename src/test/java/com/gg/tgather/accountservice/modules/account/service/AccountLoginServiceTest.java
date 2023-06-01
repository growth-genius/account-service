package com.gg.tgather.accountservice.modules.account.service;

import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample2;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createResendAuthFormWithEmailSample2;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.emailSample2;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.emailSample2Password;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.gg.tgather.accountservice.infra.annotation.ServiceTestNoRollback;
import com.gg.tgather.accountservice.infra.container.AbstractContainerBaseTest;
import com.gg.tgather.accountservice.modules.account.dto.CustomAccountDto;
import com.gg.tgather.accountservice.modules.account.entity.Account;
import com.gg.tgather.accountservice.modules.account.form.ModifyAccountForm;
import com.gg.tgather.accountservice.modules.account.repository.AccountRepository;
import com.gg.tgather.commonservice.dto.account.AccountDto;
import com.gg.tgather.commonservice.security.CredentialInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 이메일 인증 성공 후, 로그인 테스트
 */
@ServiceTestNoRollback
class AccountLoginServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    public void successAuthAccount() {
        accountService.saveAccount(createAccountSaveFormWithEmailSample2());
        accountService.validAuthCode(createResendAuthFormWithEmailSample2());
    }

    @Test
    @Order(1)
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

    @Test
    @Order(2)
    @DisplayName("사용자 정보 수정 확인")
    void modifyAccount() {
        // given
        ModifyAccountForm modifyAccountForm = new ModifyAccountForm();
        String revisedNickname = "낑깡";
        modifyAccountForm.setNickname(revisedNickname);
        Account account = accountRepository.findByEmail(emailSample2).orElseThrow();
        // when
        CustomAccountDto customAccountDto = accountService.modifyAccount(account.getAccountId(), modifyAccountForm);
        // then
        assertEquals(revisedNickname, customAccountDto.getNickname());
        assertNotNull(customAccountDto.getAccountId());

    }
}
