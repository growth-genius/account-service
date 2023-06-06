package com.gg.tgather.accountservice.modules.account.util;

import com.gg.tgather.accountservice.modules.account.form.AccountSaveForm;
import com.gg.tgather.accountservice.modules.account.form.AuthCodeForm;

public class AccountTestUtil {

    public static final String EMAIL_SAMPLE_1 = "choyeji1591@gmail.com";
    public static final String EMAIL_SAMPLE_2 = "leesg107@naver.com";

    public static final String EMAIL_SAMPLE_1_PASSWORD = "TestYeji0529!";
    public static final String EMAIL_SAMPLE_2_PASSWORD = "seungguJJang2!";
    public static final String NICKNAME = "seunggu";

    public static AccountSaveForm createAccountSaveFormWithEmailSample1() {
        AccountSaveForm accountSaveForm = new AccountSaveForm();
        accountSaveForm.setUsername("yeji");
        accountSaveForm.setPassword(EMAIL_SAMPLE_1_PASSWORD);
        accountSaveForm.setNickname("yeji");
        accountSaveForm.setBirth(961126);
        accountSaveForm.setEmail(EMAIL_SAMPLE_1);
        return accountSaveForm;
    }


    public static AccountSaveForm createAccountSaveFormWithEmailSample2() {
        AccountSaveForm accountSaveForm = new AccountSaveForm();
        accountSaveForm.setUsername(NICKNAME);
        accountSaveForm.setPassword(EMAIL_SAMPLE_2_PASSWORD);
        accountSaveForm.setNickname(NICKNAME);
        accountSaveForm.setBirth(881008);
        accountSaveForm.setEmail(EMAIL_SAMPLE_2);
        return accountSaveForm;
    }

    public static AuthCodeForm createResendAuthFormWithEmailSample1() {
        AuthCodeForm authCodeForm = new AuthCodeForm();
        authCodeForm.setAuthCode("authcode_");
        authCodeForm.setEmail(EMAIL_SAMPLE_2);
        return authCodeForm;
    }

    public static AuthCodeForm createResendAuthFormWithEmailSample2() {
        AuthCodeForm authCodeForm = new AuthCodeForm();
        authCodeForm.setAuthCode("authcode_");
        authCodeForm.setEmail(EMAIL_SAMPLE_2);
        return authCodeForm;
    }


}
