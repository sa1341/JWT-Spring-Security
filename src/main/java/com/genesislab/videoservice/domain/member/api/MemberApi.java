package com.genesislab.videoservice.domain.member.api;

import com.genesislab.videoservice.domain.member.dto.MemberResponse;
import com.genesislab.videoservice.domain.member.dto.SignUpRequest;
import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.service.MemberSearchService;
import com.genesislab.videoservice.domain.member.service.MemberSignUpService;
import com.genesislab.videoservice.domain.model.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/members")
public class MemberApi {

    private final MemberSearchService memberSearchService;
    private final MemberSignUpService memberSignUpService;

    @PostMapping
    public MemberResponse signUp(@RequestBody @Valid final SignUpRequest signUpRequest) {
        log.info("Parameter Info: {}", signUpRequest);
        Member member = memberSignUpService.doSignUp(signUpRequest);
        return new MemberResponse(member);
    }

    @GetMapping(value = "/{email}")
    public MemberResponse getMember(@PathVariable(name = "email") String email) {
        Member member = memberSearchService.searchByEmail(Email.of(email));
        return new MemberResponse(member);
    }
}
