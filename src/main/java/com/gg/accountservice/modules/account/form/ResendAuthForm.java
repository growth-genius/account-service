package com.gg.accountservice.modules.account.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResendAuthForm {

    @NotNull(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotNull(message = "계정 식별자를 입력해주세요.")
    private String accountId;

}
