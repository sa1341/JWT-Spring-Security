package com.genesislab.videoservice.domain.member.api;

import com.genesislab.videoservice.domain.member.dto.MemberResponse;
import com.genesislab.videoservice.domain.member.dto.SignInRequest;
import com.genesislab.videoservice.domain.member.dto.SignUpRequest;
import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.service.MemberSearchService;
import com.genesislab.videoservice.domain.member.service.MemberSignUpService;
import com.genesislab.videoservice.domain.token.dto.TokenDto;
import com.genesislab.videoservice.domain.token.dto.TokenRequest;
import com.genesislab.videoservice.domain.token.service.AuthTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/members")
public class MemberApi {

    private final MemberSearchService memberSearchService;
    private final MemberSignUpService memberSignUpService;
    private final AuthTokenService authTokenService;

    @PostMapping(value = "signUp")
    public MemberResponse signUp(@RequestBody @Valid final SignUpRequest signUpRequest) {
        log.debug("Parameter Info: {}", signUpRequest);
        Member member = memberSignUpService.doSignUp(signUpRequest);
        return new MemberResponse(member);
    }

    @PostMapping(value = "signIn")
    public TokenDto signIn(@RequestBody @Valid final SignInRequest signInRequest) {
        log.debug("Parameter Info: {}", signInRequest);
        Member member = memberSearchService.validateMemberInfo(signInRequest);
        TokenDto tokenDto = authTokenService.generateToken(member);
        return tokenDto;
    }

    @PostMapping(value = "/reissue")
    public TokenDto reissue(@RequestBody @Valid final TokenRequest tokenRequest) {
        return authTokenService.reissue(tokenRequest);
    }
}
