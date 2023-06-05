package com.gg.tgather.accountservice.modules.account.util;

import com.gg.tgather.accountservice.modules.account.form.AccountSaveForm;
import com.gg.tgather.accountservice.modules.account.form.AuthCodeForm;

public class AccountTestUtil {

    public static final String emailSample1 = "choyeji1591@gmail.com";
    public static final String emailSample2 = "leesg107@naver.com";

    public static final String emailSample1Password = "TestYeji0529!";
    public static final String emailSample2Password = "seungguJJang2!";
    public static final String nickname = "seunggu";

    public static AccountSaveForm createAccountSaveFormWithEmailSample1() {
        AccountSaveForm accountSaveForm = new AccountSaveForm();
        accountSaveForm.setUsername("yeji");
        accountSaveForm.setPassword(emailSample1Password);
        accountSaveForm.setNickname("yeji");
        accountSaveForm.setBirth(961126);
        accountSaveForm.setEmail(emailSample1);
        return accountSaveForm;
    }


    public static AccountSaveForm createAccountSaveFormWithEmailSample2() {
        AccountSaveForm accountSaveForm = new AccountSaveForm();
        accountSaveForm.setUsername(nickname);
        accountSaveForm.setPassword(emailSample2Password);
        accountSaveForm.setNickname(nickname);
        accountSaveForm.setBirth(881008);
        accountSaveForm.setEmail(emailSample2);
        return accountSaveForm;
    }

    public static AuthCodeForm createResendAuthFormWithEmailSample1() {
        AuthCodeForm authCodeForm = new AuthCodeForm();
        authCodeForm.setAuthCode("authcode_");
        authCodeForm.setEmail(emailSample2);
        return authCodeForm;
    }

    public static AuthCodeForm createResendAuthFormWithEmailSample2() {
        AuthCodeForm authCodeForm = new AuthCodeForm();
        authCodeForm.setAuthCode("authcode_");
        authCodeForm.setEmail(emailSample2);
        return authCodeForm;
    }


}
