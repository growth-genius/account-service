package com.gg.accountservice.modules.account.controller;

import com.gg.accountservice.modules.account.service.AccountService;
import com.gg.commonservice.annotation.RestBaseAnnotation;
import com.gg.commonservice.dto.account.AccountDto;
import com.gg.commonservice.utils.ApiUtil;
import com.gg.commonservice.utils.ApiUtil.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RestBaseAnnotation
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    /**
     * 사용자 조회
     *
     * @param accountId : 사용자 식별자
     * @return 사용자 정보
     */
    @GetMapping("{accountId}")
    public ApiResult<AccountDto> getAccount(@PathVariable String accountId) {
        return ApiUtil.success(accountService.getAccount(accountId));
    }

}
