package com.genesislab.videoservice.domain.token.repository;

import com.genesislab.videoservice.domain.member.dto.MemberResponse;
import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.repository.MemberRepository;
import com.genesislab.videoservice.domain.model.*;
import com.genesislab.videoservice.domain.token.dto.TokenDto;
import com.genesislab.videoservice.domain.token.entity.RefreshToken;
import com.genesislab.videoservice.global.auth.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RefreshTokenRepositoryTests {

    @Autowired
    private RefreshTokenRepository tokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setUp() {
        Member member = Member.builder()
                              .email(Email.of("syn1341@naver.com"))
                              .password(Password.of("wnsdud123"))
                              .phoneNumber(PhoneNumber.of("010-1423-4444"))
                              .name(Name.of("이성희"))
                              .role(Role.USER)
                              .build();

        memberRepository.save(member);
    }

    @Test
    public void refresh_토큰을_저장한다() throws Exception {
        //given
        Member member = memberRepository.findByEmail(Email.of("syn1341@naver.com")).get();

        TokenDto tokenDto = jwtTokenProvider.generateJwtToken(member);
        System.out.println(tokenDto);

        TokenDto tokenDto2 = jwtTokenProvider.generateJwtToken(member);
        System.out.println(tokenDto2);

        //when
        Member findMember = memberRepository.findById(member.getId()).get();
        System.out.println(findMember.getName().getValue());

        //then
        Assertions.assertThat(findMember.getEmail().getValue()).isEqualTo("syn1341@naver.com");
     }
}
