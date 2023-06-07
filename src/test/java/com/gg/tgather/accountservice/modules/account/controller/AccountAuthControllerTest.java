package com.gg.tgather.accountservice.modules.account.controller;

import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.EMAIL_SAMPLE_1;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample1;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample2;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createResendAuthFormWithEmailSample2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gg.tgather.accountservice.infra.annotation.EnableTestcontainers;
import com.gg.tgather.accountservice.infra.annotation.MockMvcTest;
import com.gg.tgather.accountservice.infra.container.AbstractContainerMvcTest;
import com.gg.tgather.accountservice.modules.account.form.AccountSaveForm;
import com.gg.tgather.accountservice.modules.account.form.AuthCodeForm;
import com.gg.tgather.accountservice.modules.account.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;

@MockMvcTest
@EnableTestcontainers
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class AccountAuthControllerTest extends AbstractContainerMvcTest {

    @Autowired
    private AccountService accountService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void sign_up_success() throws Exception {
        // GIVEN
        AccountSaveForm accountSaveForm = createAccountSaveFormWithEmailSample1();
        // THEN
        mockMvc.perform(post("/account/auth/sign-up").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(accountSaveForm)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 후, 이메일 인증코드 확인 성공 테스트")
    void valid_auth_code_success() throws Exception {
        // GIVEN
        AuthCodeForm authCodeForm = createResendAuthFormWithEmailSample2();
        accountService.saveAccount(createAccountSaveFormWithEmailSample2());
        // Then
        mockMvc.perform(
                post("/account/auth/confirm-authcode").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(authCodeForm)))
            .andDo(print()).andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 후, 이메일 잘못된 인증코드 전송시 실패 테스트")
    void valid_auth_code_fail() throws Exception {
        // GIVEN
        AuthCodeForm authCodeForm = new AuthCodeForm();
        authCodeForm.setEmail(EMAIL_SAMPLE_1);
        authCodeForm.setAuthCode("test_auth_code_");
        accountService.saveAccount(createAccountSaveFormWithEmailSample1());
        // Then
        mockMvc.perform(post("/account/auth/check-email").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(authCodeForm)))
            .andDo(print()).andExpect(status().is4xxClientError());
    }


}