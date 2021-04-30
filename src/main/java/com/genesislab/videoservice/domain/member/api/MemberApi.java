package com.genesislab.videoservice.domain.member.api;

import com.genesislab.videoservice.domain.member.dto.MemberResponse;
import com.genesislab.videoservice.domain.member.dto.SignInRequest;
import com.genesislab.videoservice.domain.member.dto.SignUpRequest;
import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.service.MemberSearchService;
import com.genesislab.videoservice.domain.member.service.MemberSignUpService;
import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.domain.token.dto.TokenDto;
import com.genesislab.videoservice.global.auth.JwtTokenProvider;
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

    private final MemberSearchService memberSearchService;
    private final MemberSignUpService memberSignUpService;
    private final JwtTokenProvider jwtTokenProvider;

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
        return jwtTokenProvider.generateJwtToken(new MemberResponse(member));
    }
}
