package com.gg.accountservice.modules.account.controller;

import com.gg.accountservice.modules.account.form.AccountSaveForm;
import com.gg.accountservice.modules.account.form.AuthCodeForm;
import com.gg.accountservice.modules.account.form.ResendAuthForm;
import com.gg.accountservice.modules.account.form.SignInForm;
import com.gg.accountservice.modules.account.service.AccountService;
import com.gg.commonservice.annotation.RestBaseAnnotation;
import com.gg.commonservice.dto.account.AccountDto;
import com.gg.commonservice.security.CredentialInfo;
import com.gg.commonservice.utils.ApiUtil;
import com.gg.commonservice.utils.ApiUtil.ApiResult;
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
    public ApiResult<AccountDto> addUser(@RequestBody @Valid AccountSaveForm accountSaveForm) {
        return ApiUtil.success(accountService.saveAccount(accountSaveForm));
    }

    @PostMapping("/check-email")
    public ApiResult<AccountDto> validEmailAddress(@RequestBody @Valid AuthCodeForm authCodeForm) {
        return ApiUtil.success(accountService.validAuthCode(authCodeForm));
    }

    @PostMapping("/sign-in")
    public ApiResult<AccountDto> login(@RequestBody @Valid SignInForm signInForm) {
        return ApiUtil.success(accountService.login(signInForm.getEmail(), new CredentialInfo(signInForm.getPassword())));
    }

    @PostMapping("/resend/authcode")
    public ApiResult<AccountDto> resendAuthCode(@RequestBody @Valid ResendAuthForm authCodeForm) {
        return ApiUtil.success(accountService.resendAuthCode(authCodeForm));
    }

    @GetMapping("/check-nickname/{nickname}")
    public ApiResult<Boolean> authNickname(@PathVariable String nickname) {
        return ApiUtil.success(accountService.validNickname(nickname));
    }


}
