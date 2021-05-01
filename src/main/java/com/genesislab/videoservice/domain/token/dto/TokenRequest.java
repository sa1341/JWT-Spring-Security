package com.genesislab.videoservice.domain.token.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString(of = {"accessToken", "refreshToken"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenRequest {

    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
