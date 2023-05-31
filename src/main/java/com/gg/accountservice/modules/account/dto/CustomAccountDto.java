package com.gg.accountservice.modules.account.dto;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.gg.accountservice.modules.account.entity.Account;
import com.gg.commonservice.dto.account.AccountDto;
import com.gg.commonservice.enums.AccountRole;
import com.gg.commonservice.security.Jwt;

public class CustomAccountDto extends AccountDto {

    private CustomAccountDto(Account account) {
        copyProperties(account, this, "password");
    }

    public static CustomAccountDto from(Account account) {
        return new CustomAccountDto(account);
    }

    public static CustomAccountDto createByAccountAndGenerateAccessToken(Account account, Jwt jwt) {
        CustomAccountDto accountDTO = new CustomAccountDto(account);
        accountDTO.generateAccessToken(jwt);
        return accountDTO;
    }

    public void generateAccessToken(Jwt jwt) {
        Jwt.Claims claims = Jwt.Claims.of(id, accountId, email, roles.stream().map(AccountRole::name).toArray(String[]::new));
        this.accessToken = jwt.createAccessToken(claims);
        this.refreshToken = jwt.createRefreshToken(claims);
    }
}
