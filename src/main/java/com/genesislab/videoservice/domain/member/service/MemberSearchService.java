package com.genesislab.videoservice.domain.member.service;

import com.genesislab.videoservice.domain.member.dto.SignInRequest;
import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.exception.EmailNotFoundException;
import com.genesislab.videoservice.domain.member.exception.PasswordNotMatchException;
import com.genesislab.videoservice.domain.member.repository.MemberRepository;
import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.domain.model.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberSearchService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member searchByEmail(final Email email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        member.orElseThrow(() -> new EmailNotFoundException(email.getValue()));
        return member.get();
    }

    public Member validateMemberInfo(final SignInRequest signInRequest) {
        Email email = signInRequest.getEmail();
        Member member = searchByEmail(email);

        Password password = signInRequest.getPassword();
        boolean isMatched = passwordEncoder.matches(password.getValue(), member.getPassword().getValue());

        if (!isMatched) {
            throw new PasswordNotMatchException();
        }

        return member;
    }
}
