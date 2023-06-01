package com.gg.tgather.accountservice.modules.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TokenDto {

    private String accessToken;

    private String refreshToken;

    public TokenDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
