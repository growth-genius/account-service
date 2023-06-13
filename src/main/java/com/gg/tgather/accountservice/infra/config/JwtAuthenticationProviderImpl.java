package com.gg.tgather.accountservice.infra.config;

import com.gg.tgather.accountservice.modules.account.form.SignInForm;
import com.gg.tgather.accountservice.modules.account.service.AccountService;
import com.gg.tgather.commonservice.dto.account.AccountDto;
import com.gg.tgather.commonservice.security.CredentialInfo;
import com.gg.tgather.commonservice.security.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationProviderImpl implements JwtAuthenticationProvider {

    private final AccountService accountService;

    @Override
    public AccountDto getAccountDto(String principal, CredentialInfo credential) {
        return accountService.login(new SignInForm(principal, credential.getCredential()));
    }

}
