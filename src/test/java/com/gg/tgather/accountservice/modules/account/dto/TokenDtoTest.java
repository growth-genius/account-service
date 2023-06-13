package com.gg.tgather.accountservice.modules.account.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenDtoTest {


    @Test
    @DisplayName("토큰 dto 생성 테스트")
    void test_case_1() throws Exception {
        // given
        TokenDto tokenDto = new TokenDto();

        // when
        String accessToken = "accessToken";
        tokenDto.setAccessToken(accessToken);
        String refreshToken = "refreshToken";
        tokenDto.setRefreshToken(refreshToken);

        // then
        assertEquals(accessToken, tokenDto.getAccessToken());
        assertEquals(refreshToken, tokenDto.getRefreshToken());
        // when

        // then

    }
}