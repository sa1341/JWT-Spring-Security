package com.genesislab.videoservice.global.util;

import com.genesislab.videoservice.domain.token.dto.TokenDto;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static void createCookieForJwt(final TokenDto tokenDto, final HttpServletResponse response) {
        Cookie accessCookie = new Cookie("accessToken", tokenDto.getAccessToken());
        Cookie refreshCookie = new Cookie("refreshToken", tokenDto.getRefreshToken());

        accessCookie.setPath("/");
        accessCookie.setMaxAge(600);
        accessCookie.setHttpOnly(true);

        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(600);
        refreshCookie.setHttpOnly(true);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }
}
