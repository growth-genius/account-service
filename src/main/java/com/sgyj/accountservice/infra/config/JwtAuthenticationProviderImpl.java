package com.sgyj.accountservice.infra.config;

import com.sgyj.accountservice.modules.account.service.AccountService;
import com.sgyj.commonservice.dto.account.AccountDto;
import com.sgyj.commonservice.security.CredentialInfo;
import com.sgyj.commonservice.security.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationProviderImpl implements JwtAuthenticationProvider {

    private final AccountService accountService;

    @Override
    public AccountDto getAccountDto(String principal, CredentialInfo credential) {
        return accountService.login( principal, credential );
    }

}
