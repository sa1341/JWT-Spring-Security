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
    private String token;

    private TokenDto(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public static TokenDto of(String email, String token) {
        return new TokenDto(email, token);
    }
}
