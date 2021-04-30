package com.genesislab.videoservice.global.auth;

import com.genesislab.videoservice.domain.member.dto.MemberResponse;
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

    private long tokenValidTime = 10 * 60 * 1000L;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    public TokenDto generateJwtToken(final MemberResponse memberResponse) {
        Date now = new Date();
        String jwt = Jwts.builder()
                .setHeaderParams(createHeader())
                .setClaims(createClaims(memberResponse))
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        TokenDto tokenDto = TokenDto.of(memberResponse.getEmail().getValue(), jwt);
        return tokenDto;
    }

    private String getUserEmail(String jwt) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt).getBody().get("id", String.class);
    }

    public Authentication getAuthentication(String jwt) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public UserDetails getUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean isValid(String jwt) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt);
            return !claims.getBody().getExpiration().before(new Date());
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

    private Map<String, Object> createClaims(final MemberResponse memberResponse) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", memberResponse.getEmail().getValue());
        claims.put("username", memberResponse.getName().getValue());
        return claims;
    }
}
