package com.gg.accountservice.infra.config;

import com.gg.accountservice.modules.account.service.AccountService;
import com.gg.commonservice.dto.account.AccountDto;
import com.gg.commonservice.security.CredentialInfo;
import com.gg.commonservice.security.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationProviderImpl implements JwtAuthenticationProvider {

    private final AccountService accountService;

    @Override
    public AccountDto getAccountDto(String principal, CredentialInfo credential) {
        return accountService.login(principal, credential);
    }

}
