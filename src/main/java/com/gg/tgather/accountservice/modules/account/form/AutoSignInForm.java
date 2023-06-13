package com.gg.tgather.accountservice.modules.account.form;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AutoSignInForm {

    @NotNull(message = "[refreshToken]은 필수 입니다.")
    private String refreshToken;

    @NotNull(message = "fcmToken은 필수 입니다.")
    private String fcmToken;

}
