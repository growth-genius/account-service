package com.sgyj.accountservice.infra.security;

import com.sgyj.accountservice.modules.account.enums.LoginType;
import lombok.Data;

@Data
public class CredentialInfo {

    private String credential;
    private LoginType loginType;

    public CredentialInfo ( String password, LoginType loginType ) {
        this.credential = password;
        this.loginType = loginType;
    }

    public CredentialInfo ( String password ) {
        this.credential = password;
        this.loginType = LoginType.TGAHTER;
    }

}
