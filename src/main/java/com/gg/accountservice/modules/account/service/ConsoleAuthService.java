package com.gg.accountservice.modules.account.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile({"local", "test"})
@Component
public class ConsoleAuthService implements AuthService {

    @Override
    public String createAuthCode() {
        return "authcode_";
    }
}