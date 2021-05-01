package com.genesislab.videoservice.domain.token.service;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.domain.model.Name;
import com.genesislab.videoservice.domain.model.Password;
import com.genesislab.videoservice.domain.model.PhoneNumber;
import com.genesislab.videoservice.domain.token.dto.TokenDto;
import com.genesislab.videoservice.global.auth.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JwtTokenProviderTests {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final static String email = "a79007714@gmail.com";

    private Member member;

    @BeforeEach
    public void setUp() {
        member = Member.builder()
                .email(Email.of("syn1341@gmail.com"))
                .password(Password.of("wnsdud123"))
                .name(Name.of("임준영"))
                .phoneNumber(PhoneNumber.of("010-7900-7714"))
                .build();
    }

    @Test
    public void jwt_토큰을_발급한다() throws Exception {
        //given
        //when
        TokenDto tokenDto = jwtTokenProvider.generateJwtToken(member);

        System.out.println(tokenDto);
        //then
        Assertions.assertThat(tokenDto.getEmail()).isEqualTo(email);
    }

    @Test
    public void jwt_토큰을_검증한다() throws Exception {
        //when
        TokenDto tokenDto = jwtTokenProvider.generateJwtToken(member);

        if (!jwtTokenProvider.isExpiredToken(tokenDto.getAccessToken())) {
            System.out.println("토큰이 유효함");
        } else {
            System.out.println("토큰이 유효하지는 않음");
        }
        //then
    }
}
