package com.gg.tgather.accountservice.modules.account.controller;

import static com.gg.tgather.commonservice.utils.ApiUtil.success;

import com.gg.tgather.accountservice.modules.account.dto.ModifyAccountDto;
import com.gg.tgather.accountservice.modules.account.form.ModifyAccountForm;
import com.gg.tgather.accountservice.modules.account.service.AccountService;
import com.gg.tgather.commonservice.annotation.RestBaseAnnotation;
import com.gg.tgather.commonservice.dto.account.AccountDto;
import com.gg.tgather.commonservice.enums.EnumMapperValue;
import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.commonservice.utils.ApiUtil.ApiResult;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    @GetMapping("/{accountId}")
    public AccountDto getAccount(@PathVariable String accountId) {
        return accountService.getAccount(accountId);
    }


    /**
     * 계정 정보 수정
     *
     * @param accountId         사용자 식별자
     * @param modifyAccountForm 수정 입력 폼
     * @return 사용자 정보
     */
    @PatchMapping("/{accountId}")
    public ApiResult<AccountDto> modifyAccount(@PathVariable String accountId, @Valid @RequestBody ModifyAccountForm modifyAccountForm) {
        return success(accountService.modifyAccount(accountId, modifyAccountForm));
    }

    /**
     * 사용자 목록 조회
     *
     * @param accountIds 조회할 사용자 아이디 목록
     * @return List<AccountDto> 조회된 사용자 목록</AccountDto>
     */
    @GetMapping
    public ApiResult<List<? extends AccountDto>> getAccounts(List<String> accountIds) {
        return success(accountService.getAccounts(accountIds));
    }

    /**
     * 로그인 사용자의 수정 정보 조회
     *
     * @param authentication 로그인 사용자
     * @return ModifyAccountDto
     */
    @GetMapping("/me")
    public ApiResult<ModifyAccountDto> getMyAccountInfo(@AuthenticationPrincipal JwtAuthentication authentication) {
        return success(ModifyAccountDto.builder().accountDto(accountService.getByAccount(authentication))
            .travelThemes(Arrays.stream(TravelTheme.values()).map(EnumMapperValue::new).toList()).build());
    }


}
