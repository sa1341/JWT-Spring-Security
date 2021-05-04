package com.genesislab.videoservice.domain.member.api;

import com.genesislab.videoservice.domain.member.dto.MemberResponse;
import com.genesislab.videoservice.domain.member.dto.MemberUpdateReq;
import com.genesislab.videoservice.domain.member.dto.SignInRequest;
import com.genesislab.videoservice.domain.member.dto.SignUpRequest;
import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.service.MemberService;
import com.genesislab.videoservice.domain.member.service.MemberSignUpService;
import com.genesislab.videoservice.domain.token.dto.TokenDto;
import com.genesislab.videoservice.domain.token.dto.TokenRequest;
import com.genesislab.videoservice.domain.token.service.AuthTokenService;
import com.genesislab.videoservice.global.auth.JwtTokenProvider;
import com.genesislab.videoservice.global.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/members")
public class MemberApi {

    private final MemberService memberService;
    private final MemberSignUpService memberSignUpService;
    private final AuthTokenService authTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입 기능 구현
    @PostMapping(value = "signUp")
    public MemberResponse signUp(@RequestBody @Valid final SignUpRequest signUpRequest) {
        log.debug("Parameter Info: {}", signUpRequest);
        Member member = memberSignUpService.doSignUp(signUpRequest);
        return new MemberResponse(member);
    }

    // 로그인 기능
    @PostMapping(value = "signIn")
    public TokenDto signIn(@RequestBody @Valid final SignInRequest signInRequest, HttpServletResponse response) {
        log.debug("Parameter Info: {}", signInRequest);
        Member member = memberService.validateMemberInfo(signInRequest);
        TokenDto tokenDto = authTokenService.generateToken(member);
        CookieUtils.createCookieForJwt(tokenDto, response);
        return tokenDto;
    }

    // JTW 토큰 리프레시 서비스
    @PostMapping(value = "/reissue")
    public TokenDto reissue(@RequestBody @Valid final TokenRequest tokenRequest) {
        return authTokenService.reissue(tokenRequest);
    }

    // 회원 탈퇴 기능 (상태값 업데이트 수행)
    @DeleteMapping
    public ResponseEntity<String> unsubscribe(@CookieValue(name = "accessToken", required = true) final String token) {
        String email = jwtTokenProvider.getUserEmail(token);
        memberService.unsubscribe(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 회원 프로필 수정
    @PutMapping
    public MemberUpdateReq updateProfile(@CookieValue(name = "accessToken", required = true) final String token,
                                         @RequestBody final MemberUpdateReq updateReq) {
        String email = jwtTokenProvider.getUserEmail(token);
        MemberUpdateReq memberUpdateReq = memberService.updateProfile(email, updateReq);
        return memberUpdateReq;
    }
}
