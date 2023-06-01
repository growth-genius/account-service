package com.gg.tgather.accountservice.modules.account.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignInForm {

    @NotNull(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotNull(message = "비밀번호 입력해 주세요.")
    private String password;


}
