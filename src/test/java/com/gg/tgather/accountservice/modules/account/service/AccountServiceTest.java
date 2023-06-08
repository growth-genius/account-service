package com.gg.tgather.accountservice.modules.account.service;

import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.EMAIL_SAMPLE_1;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.EMAIL_SAMPLE_2;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.NICKNAME;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample1;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample2;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gg.tgather.accountservice.infra.annotation.EnableTestcontainers;
import com.gg.tgather.accountservice.infra.annotation.ServiceTest;
import com.gg.tgather.accountservice.modules.account.entity.Account;
import com.gg.tgather.accountservice.modules.account.repository.AccountRepository;
import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.commonservice.dto.account.AccountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.test.context.EmbeddedKafka;

/**
 * 회원가입 테스트
 */
@ServiceTest
@EnableTestcontainers
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9094", "port=9094"})
class AccountServiceTest {

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
    void whenNicknameExist_thenReturnTrue() {
        // when
        boolean validNickname = accountService.validNickname(NICKNAME);
        // then
        assertTrue(validNickname);
    }

    @Test
    @Order(1)
    @DisplayName("이메일 중복 여부 확인")
    void whenEmailExist_thenReturnTrue() {
        // when
        Boolean validEmailAddress = accountService.validEmailAddress(EMAIL_SAMPLE_2);
        // then
        assertTrue(validEmailAddress);
    }

    @Test
    @Order(2)
    @DisplayName("이메일 유효성 검사 확인")
    void whenInCorrectEmail_thenExceptionThrows() {
        // given
        String email = "test";
        // when, then
        String responseMessage = assertThrows(OmittedRequireFieldException.class, () -> accountService.validEmailAddress(email)).getMessage();
        assertEquals("이메일 형식이 올바르지 않습니다.", responseMessage);
    }

    @Test
    @Order(3)
    @DisplayName("회원가입 확인")
    void whenSignup_thenSuccess() {
        AccountDto accountDto = accountService.saveAccount(createAccountSaveFormWithEmailSample1());
        assertEquals(EMAIL_SAMPLE_1, accountDto.getEmail());
    }

}