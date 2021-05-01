package com.genesislab.videoservice.global.auth;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.service.MemberSearchService;
import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.domain.token.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 10;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public TokenDto generateJwtToken(final Member member) {
        long now = (new Date()).getTime();
        // AccessToken 발급
        String jwt = createAccessToken(member, now);
        // refreshToken 발급
        String refreshToken = createRefreshToken(member, now);

        String email = member.getEmail().getValue();
        TokenDto tokenDto = TokenDto.of(email, jwt, refreshToken);
        return tokenDto;
    }

    private String createRefreshToken(final Member member, final long now) {
        Date refreshTokenExpiredTime = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiredTime)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return refreshToken;
    }

    private String createAccessToken(final Member member, long now) {
        String jwt = Jwts.builder()
                .setHeaderParams(createHeader())
                .setClaims(createClaims(member))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return jwt;
    }

    public String getUserEmail(final String jwt) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody().get("id", String.class);
    }

    public Authentication getAuthentication(final String jwt) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean isExpiredToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
             return claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");
        return headers;
    }

    private Map<String, Object> createClaims(final Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", member.getEmail().getValue());
        claims.put("username", member.getName().getValue());
        return claims;
    }
}
