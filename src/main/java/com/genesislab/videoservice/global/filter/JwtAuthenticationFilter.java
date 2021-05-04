package com.genesislab.videoservice.global.filter;

import com.genesislab.videoservice.global.auth.JwtTokenProvider;
import com.genesislab.videoservice.global.error.exception.ErrorCode;
import com.genesislab.videoservice.global.error.exception.InvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String HEAD_AUTH = "Authorization";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws InvalidException, IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Cookie[] cookies = httpServletRequest.getCookies();
        Cookie accessCookie = null;
        String jwt = "";

        if (cookies != null) {
            accessCookie = Arrays.asList(cookies)
                    .stream()
                    .filter(cookie -> "accessToken".equalsIgnoreCase(cookie.getName()))
                    .findFirst()
                    .orElseThrow(() -> new InvalidException(ErrorCode.INVALID_INPUT_VALUE));
            jwt = accessCookie.getValue();
        }

        if (StringUtils.hasText(jwt) && !jwtTokenProvider.isExpiredToken(jwt)) {
            Authentication auth = jwtTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }


        chain.doFilter(request, response);
    }
}
