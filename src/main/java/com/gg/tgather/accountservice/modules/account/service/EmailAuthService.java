package com.gg.tgather.accountservice.modules.account.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile({"dev", "prod"})
@Component
public class EmailAuthService implements AuthService {

    @Override
    public String createAuthCode() {

        return RandomStringUtils.randomAlphanumeric(12);
    }
}
