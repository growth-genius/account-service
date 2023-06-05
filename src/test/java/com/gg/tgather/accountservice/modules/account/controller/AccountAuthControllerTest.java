package com.gg.tgather.accountservice.modules.account.controller;

import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample1;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createAccountSaveFormWithEmailSample2;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.createResendAuthFormWithEmailSample2;
import static com.gg.tgather.accountservice.modules.account.util.AccountTestUtil.emailSample1;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gg.tgather.accountservice.infra.annotation.MockMvcTest;
import com.gg.tgather.accountservice.infra.container.AbstractContainerMvcTest;
import com.gg.tgather.accountservice.modules.account.form.AccountSaveForm;
import com.gg.tgather.accountservice.modules.account.form.AuthCodeForm;
import com.gg.tgather.accountservice.modules.account.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@MockMvcTest
@SpringBootTest
class AccountAuthControllerTest extends AbstractContainerMvcTest {

    @Autowired
    private AccountService accountService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void test_case_1() throws Exception {
        // GIVEN
        AccountSaveForm accountSaveForm = createAccountSaveFormWithEmailSample1();
        // THEN
        mockMvc.perform(post("/account/auth/sign-up").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(accountSaveForm)))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 후, 이메일 인증코드 확인 성공 테스트")
    void test_case_2() throws Exception {
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
    void test_case_3() throws Exception {
        // GIVEN
        AuthCodeForm authCodeForm = new AuthCodeForm();
        authCodeForm.setEmail(emailSample1);
        authCodeForm.setAuthCode("test_auth_code_");
        accountService.saveAccount(createAccountSaveFormWithEmailSample1());
        // Then
        mockMvc.perform(post("/account/auth/check-email").contentType(MediaType.APPLICATION_JSON).content(this.objectMapper.writeValueAsString(authCodeForm)))
            .andDo(print()).andExpect(status().is4xxClientError());
    }


}