package com.gg.tgather.accountservice.modules.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.gg.tgather.accountservice.infra.annotation.ServiceTest;
import com.gg.tgather.accountservice.infra.container.AbstractContainerBaseTest;
import com.gg.tgather.accountservice.modules.account.dto.CustomAccountDto;
import com.gg.tgather.accountservice.modules.account.entity.Account;
import com.gg.tgather.accountservice.modules.account.form.ModifyAccountForm;
import com.gg.tgather.accountservice.modules.account.repository.AccountRepository;
import com.gg.tgather.accountservice.modules.account.util.AccountTestUtil;
import com.gg.tgather.commonservice.dto.account.AccountDto;
import com.gg.tgather.commonservice.security.CredentialInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 사용자 로그인 후, 정보수정 테스트
 */
@ServiceTest
class AccountLoginServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void successAuthAccount() {
        accountService.saveAccount(AccountTestUtil.createAccountSaveFormWithEmailSample2());
        accountService.validAuthCode(AccountTestUtil.createResendAuthFormWithEmailSample2());
    }

    @Test
    @Order(1)
    @DisplayName("로그인 성공 확인")
    void successLogin() {
        // given
        CredentialInfo credentialInfo = new CredentialInfo(AccountTestUtil.emailSample2Password);
        // when
        AccountDto loginAccount = accountService.login(AccountTestUtil.emailSample2, credentialInfo);
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
        Account account = accountRepository.findByEmail(AccountTestUtil.emailSample2).orElseThrow();
        // when
        CustomAccountDto customAccountDto = accountService.modifyAccount(account.getAccountId(), modifyAccountForm);
        // then
        assertEquals(revisedNickname, customAccountDto.getNickname());
        assertNotNull(customAccountDto.getAccountId());

    }

}
