package com.gg.tgather.accountservice.modules.account.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthCodeForm {

    @NotNull(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Size(max = 320, message = "유효한 이메일 주소 길이를 벗어났습니다.")
    private String email;

    private String authCode;

}
