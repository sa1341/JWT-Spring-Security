package com.genesislab.videoservice.domain.auth;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.model.Password;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PasswordEncodingTests {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void password_암호화_비교_테스트() throws Exception {
        //given
        String password = "wnsdud2";
        String encodedPassword = passwordEncoder.encode("wnsdud2");

        //when
        boolean isMatched = passwordEncoder.matches(password, encodedPassword);
        //then
        assertThat(isMatched).isTrue();
     }
}
