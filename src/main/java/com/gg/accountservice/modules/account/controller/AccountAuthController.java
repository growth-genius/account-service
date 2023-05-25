package com.gg.accountservice.modules.account.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gg.accountservice.modules.account.form.AccountSaveForm;
import com.gg.accountservice.modules.account.form.AuthCodeForm;
import com.gg.accountservice.modules.account.form.SignInForm;
import com.gg.accountservice.modules.account.service.AccountService;
import com.sgyj.commonservice.annotation.RestBaseAnnotation;
import com.sgyj.commonservice.dto.account.AccountDto;
import com.sgyj.commonservice.security.CredentialInfo;
import com.sgyj.commonservice.utils.ApiUtil;
import com.sgyj.commonservice.utils.ApiUtil.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestBaseAnnotation
@RequiredArgsConstructor
@RequestMapping("/account/auth")
public class AccountAuthController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    public ApiResult<AccountDto> addUser(@RequestBody @Valid AccountSaveForm accountSaveForm) throws JsonProcessingException {
        return ApiUtil.success(accountService.saveAccount(accountSaveForm));
    }

    @PostMapping("/check-email")
    public ApiResult<AccountDto> authCode(@RequestBody @Valid AuthCodeForm authCodeForm) {
        return ApiUtil.success(accountService.validAuthCode(authCodeForm));
    }

    @PostMapping("/sign-in")
    public ApiResult<AccountDto> login(@RequestBody @Valid SignInForm signInForm) {
        return ApiUtil.success(accountService.login(signInForm.getEmail(), new CredentialInfo(signInForm.getPassword())));
    }

    @GetMapping("/check-nickname/{nickname}")
    public ApiResult<Boolean> authNickname(@PathVariable String nickname) {
        return ApiUtil.success(accountService.validNickname(nickname));
    }

}
