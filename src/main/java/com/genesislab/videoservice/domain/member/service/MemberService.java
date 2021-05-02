package com.genesislab.videoservice.domain.member.service;

import com.genesislab.videoservice.domain.member.dto.MemberUpdateReq;
import com.genesislab.videoservice.domain.member.dto.SignInRequest;
import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.exception.EmailNotFoundException;
import com.genesislab.videoservice.domain.member.exception.PasswordNotMatchException;
import com.genesislab.videoservice.domain.member.repository.MemberRepository;
import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.domain.model.Password;
import com.genesislab.videoservice.global.error.exception.ErrorCode;
import com.genesislab.videoservice.global.error.exception.InvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void unsubscribe(final String email) {
        if (!StringUtils.hasText(email)) throw new InvalidException(ErrorCode.INVALID_INPUT_VALUE);
        Optional<Member> optional = memberRepository.findByEmail(Email.of(email));
        optional.orElseThrow(() -> new EmailNotFoundException(email));
        final Member member = optional.get();
        member.changeUnsubscribableStatus();
    }

    @Transactional
    public MemberUpdateReq updateProfile(final String email, final MemberUpdateReq updateReq) {
        final Member member = memberRepository.findByEmail(Email.of(email)).get();
        member.updateProfile(updateReq.getName());
        return updateReq;
    }

    public Member validateMemberInfo(final SignInRequest signInRequest) {
        Email email = signInRequest.getEmail();
        final Member member = searchByEmail(email);

        Password password = signInRequest.getPassword();
        boolean isMatched = passwordEncoder.matches(password.getValue(), member.getPassword().getValue());

        if (!isMatched) {
            throw new PasswordNotMatchException();
        }

        return member;
    }

    public Member searchByEmail(final Email email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        member.orElseThrow(() -> new EmailNotFoundException(email.getValue()));
        return member.get();
    }
}
