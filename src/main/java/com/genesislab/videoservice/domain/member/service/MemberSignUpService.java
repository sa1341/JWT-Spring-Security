package com.genesislab.videoservice.domain.member.service;

import com.genesislab.videoservice.domain.member.dto.SignUpRequest;
import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.exception.EmailDuplicateException;
import com.genesislab.videoservice.domain.member.repository.MemberRepository;
import com.genesislab.videoservice.domain.model.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberSignUpService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member doSignUp(final SignUpRequest signUpRequest) {

        Email email = signUpRequest.getEmail();

        if (memberRepository.existsByEmail(email)) {
            throw new EmailDuplicateException(email);
        }

        Member member = signUpRequest.toEntity();
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword().getValue());
        member.encodePassword(encodedPassword);

        return memberRepository.save(member);
    }
}
