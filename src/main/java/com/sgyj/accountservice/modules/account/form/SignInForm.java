package com.sgyj.accountservice.modules.account.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SignInForm {

    @NotNull( message = "이메일을 입력해 주세요." )
    @Email( message = "이메일 형식이 올바르지 않습니다." )
    @Min(value = 5)
    @Max(value = 10)
    private String email;

    @Min(value = 6)
    @Max(value = 20)
    @NotNull( message = "비밀번호 입력해 주세요." )
    private String password;


}
