package com.gg.tgather.accountservice.modules.account.controller;

import com.gg.tgather.accountservice.modules.account.form.AccountSaveForm;
import com.gg.tgather.accountservice.modules.account.form.AuthCodeForm;
import com.gg.tgather.accountservice.modules.account.form.ResendAuthForm;
import com.gg.tgather.accountservice.modules.account.form.SignInForm;
import com.gg.tgather.accountservice.modules.account.service.AccountService;
import com.gg.tgather.commonservice.annotation.RestBaseAnnotation;
import com.gg.tgather.commonservice.dto.account.AccountDto;
import com.gg.tgather.commonservice.security.CredentialInfo;
import com.gg.tgather.commonservice.utils.ApiUtil;
import com.gg.tgather.commonservice.utils.ApiUtil.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * account CRUD api
 *
 * @author joyeji
 * @since 2023.06.05
 */
@RestBaseAnnotation
@RequiredArgsConstructor
@RequestMapping("/account/auth")
public class AccountAuthController {

    private final AccountService accountService;

    /**
     * 회원가입
     *
     * @param accountSaveForm 회원가입 입력폼
     * @return AccountDto 회원가입 성공 계정 정보
     */
    @PostMapping("/sign-up")
    public ApiResult<AccountDto> addUser(@RequestBody @Valid AccountSaveForm accountSaveForm) {
        return ApiUtil.success(accountService.saveAccount(accountSaveForm));
    }

    /**
     * 인증코드 확인
     *
     * @param authCodeForm 인증코드 입력폼
     * @return AccountDto 인증코드 인증 성공 정보
     */
    @PostMapping("/confirm-authcode")
    public ApiResult<AccountDto> validEmailByAuthCode(@RequestBody @Valid AuthCodeForm authCodeForm) {
        return ApiUtil.success(accountService.validAuthCode(authCodeForm));
    }

    /**
     * 로그인
     *
     * @param signInForm 로그인폼
     * @return AccountDto 로그인 계정 정보
     */
    @PostMapping("/sign-in")
    public ApiResult<AccountDto> login(@RequestBody @Valid SignInForm signInForm) {
        return ApiUtil.success(accountService.login(signInForm.getEmail(), new CredentialInfo(signInForm.getPassword())));
    }

    /**
     * 이메일 인증코드 재전송
     *
     * @param authCodeForm 이메일인증 폼
     * @return Boolean 이메일인증코드 재전송 결과값
     */
    @PostMapping("/resend/authcode")
    public ApiResult<Boolean> resendAuthCode(@RequestBody @Valid ResendAuthForm authCodeForm) {
        return ApiUtil.success(accountService.resendAuthCode(authCodeForm));
    }

    /**
     * 닉네임 인증
     *
     * @param nickname 닉네임명
     * @return Boolean 닉네임 유효성 결과값
     */
    @GetMapping("/check-nickname/{nickname}")
    public ApiResult<Boolean> authNickname(@PathVariable String nickname) {
        return ApiUtil.success(accountService.validNickname(nickname));
    }

    /**
     * 이메일 인증
     *
     * @param email 이메일 입력폼
     * @return Boolean 이메일 유효성 결과값
     */
    @GetMapping("/check-email/{email}")
    public ApiResult<Boolean> validEmailAddress(@PathVariable String email) {
        return ApiUtil.success(accountService.validEmailAddress(email));
    }

}
