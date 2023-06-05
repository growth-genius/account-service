package com.gg.tgather.accountservice.modules.account.service;

import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample1;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample2;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.emailSample1;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.emailSample2;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.nickname;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gg.tgather.accountservice.infra.annotation.ServiceTest;
import com.gg.tgather.accountservice.infra.container.AbstractContainerBaseTest;
import com.gg.tgather.accountservice.modules.account.entity.Account;
import com.gg.tgather.accountservice.modules.account.repository.AccountRepository;
import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.commonservice.dto.account.AccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 회원가입 테스트
 */
@ServiceTest
class AccountServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void saveAccount() {
        accountRepository.save(Account.createAccount(createAccountSaveFormWithEmailSample2()));
    }

    @Test
    @Order(0)
    @DisplayName("닉네임 중복 여부 확인")
    void validNickname() {
        // when
        boolean validNickname = accountService.validNickname(nickname);
        // then
        assertTrue(validNickname);
    }

    @Test
    @Order(1)
    @DisplayName("이메일 중복 여부 확인")
    void isExistEmail() {
        // when
        Boolean validEmailAddress = accountService.validEmailAddress(emailSample2);
        // then
        assertTrue(validEmailAddress);
    }

    @Test
    @Order(2)
    @DisplayName("이메일 유효성 검사 확인")
    void validEmail() {
        // given
        String email = "test";
        // when, then
        String responseMessage = assertThrows(OmittedRequireFieldException.class, () -> accountService.validEmailAddress(email)).getMessage();
        assertEquals("이메일 형식이 올바르지 않습니다.", responseMessage);
    }

    @Test
    @Order(3)
    @DisplayName("회원가입 확인")
    void test_case_1() {
        AccountDto accountDto = accountService.saveAccount(createAccountSaveFormWithEmailSample1());
        assertEquals(emailSample1, accountDto.getEmail());
    }

}