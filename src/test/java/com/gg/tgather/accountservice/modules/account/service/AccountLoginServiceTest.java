package com.gg.tgather.accountservice.modules.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.gg.tgather.accountservice.infra.annotation.EnableTestcontainers;
import com.gg.tgather.accountservice.infra.annotation.ServiceTest;
import com.gg.tgather.accountservice.infra.container.AbstractContainerMvcTest;
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
@EnableTestcontainers
class AccountLoginServiceTest extends AbstractContainerMvcTest {

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
    void whenLoginUser_thenSuccess() {
        // given
        CredentialInfo credentialInfo = new CredentialInfo(AccountTestUtil.EMAIL_SAMPLE_2_PASSWORD);
        // when
        AccountDto loginAccount = accountService.login(AccountTestUtil.EMAIL_SAMPLE_2, credentialInfo);
        // then
        assertEquals(1, loginAccount.getLoginCount());
        assertNotNull(loginAccount.getRefreshToken());
    }

    @Test
    @Order(2)
    @DisplayName("사용자 정보 수정 확인")
    void whenModifyUser_thenSuccess() {
        // given
        ModifyAccountForm modifyAccountForm = new ModifyAccountForm();
        String revisedNickname = "낑깡";
        modifyAccountForm.setNickname(revisedNickname);
        Account account = accountRepository.findByEmail(AccountTestUtil.EMAIL_SAMPLE_2).orElseThrow();
        // when
        CustomAccountDto customAccountDto = accountService.modifyAccount(account.getAccountId(), modifyAccountForm);
        // then
        assertEquals(revisedNickname, customAccountDto.getNickname());
        assertNotNull(customAccountDto.getAccountId());

    }

}
