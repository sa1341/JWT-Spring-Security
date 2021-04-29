package com.genesislab.videoservice.domain.member.api;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.repository.MemberRepository;
import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.domain.model.Name;
import com.genesislab.videoservice.domain.model.Password;
import com.genesislab.videoservice.domain.model.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void setUp() {
        Member member = Member.builder()
                        .email(Email.of("a79007714@gmail.com"))
                        .name(Name.of("임준영"))
                        .password(Password.of("wnsdud@123"))
                        .phoneNumber(PhoneNumber.of("010-7900-7714"))
                        .build();

        memberRepository.save(member);
    }

    @Test
    public void 회원_이메일을_조회한다() throws Exception {
        //given
        String email = "a79007714@gmail.com";
        ResultActions result = mockMvc.perform(get("/api/members/{email}", email).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andDo(print());

    }
}
