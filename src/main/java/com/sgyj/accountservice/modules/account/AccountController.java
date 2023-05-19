package com.sgyj.accountservice.modules.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sgyj.accountservice.infra.security.CredentialInfo;
import com.sgyj.accountservice.modules.account.dto.AccountDto;
import com.sgyj.accountservice.modules.account.form.AccountSaveForm;
import com.sgyj.accountservice.modules.account.form.AuthCodeForm;
import com.sgyj.accountservice.modules.account.form.SignInForm;
import com.sgyj.accountservice.modules.account.service.AccountService;
import com.sgyj.accountservice.modules.common.annotation.RestBaseAnnotation;
import com.sgyj.accountservice.modules.utils.ApiUtil;
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
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    public ApiUtil.ApiResult<AccountDto> addUser(@RequestBody @Valid AccountSaveForm accountSaveForm) throws JsonProcessingException {
        return ApiUtil.success(accountService.saveAccount(accountSaveForm));
    }

    @PostMapping("/check-email")
    public ApiUtil.ApiResult<AccountDto> authCode(@RequestBody @Valid AuthCodeForm authCodeForm) {
        return ApiUtil.success(accountService.validAuthCode(authCodeForm));
    }

    @PostMapping("/login")
    public ApiUtil.ApiResult<AccountDto> login(@RequestBody @Valid SignInForm signInForm) {
        return ApiUtil.success(accountService.login(signInForm.getEmail(), new CredentialInfo(signInForm.getPassword())));
    }

    @GetMapping("/check-nickname/{nickname}")
    public ApiUtil.ApiResult<Boolean> authNickname(@PathVariable String nickname) {
        return ApiUtil.success(accountService.validNickname(nickname));
    }


}
