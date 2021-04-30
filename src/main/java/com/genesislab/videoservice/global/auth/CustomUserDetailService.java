package com.genesislab.videoservice.global.auth;

import com.genesislab.videoservice.domain.member.entity.CustomUserDetails;
import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.exception.MemberNotFoundException;
import com.genesislab.videoservice.domain.member.repository.MemberRepository;
import com.genesislab.videoservice.domain.model.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = memberRepository.findByEmail(Email.of(username));
        member.orElseThrow(() -> new MemberNotFoundException(username));
        return CustomUserDetails.of(member.get());
    }
}
