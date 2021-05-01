package com.genesislab.videoservice.domain.token.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(of = {"token", "email"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenDto {

    private String email;
    private String accessToken;
    private String refreshToken;

    private TokenDto(String email, String accessToken, String refreshToken) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static TokenDto of(String email, String accessToken, String refreshToken) {
        return new TokenDto(email, accessToken, refreshToken);
    }
}
