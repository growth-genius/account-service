package com.gg.tgather.accountservice.modules.account.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmailAuthForm {

    @NotNull(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    private EmailAuthForm(String email) {
        this.email = email;
    }

    public static EmailAuthForm createFormForTest(String emailSample2) {
        return new EmailAuthForm(emailSample2);
    }
}
