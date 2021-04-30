package com.genesislab.videoservice.domain.member.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genesislab.videoservice.domain.member.dto.MemberResponse;
import com.genesislab.videoservice.domain.member.dto.SignUpRequest;
import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.repository.MemberRepository;
import com.genesislab.videoservice.domain.member.service.MemberSignUpService;
import com.genesislab.videoservice.domain.model.*;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;



@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private MemberSignUpService memberSignUpService;

    @BeforeEach
    public void setUp() {
        Member member = Member.builder()
                        .email(Email.of("a79007714@gmail.com"))
                        .name(Name.of("임준영"))
                        .password(Password.of("wnsdud@123"))
                        .phoneNumber(PhoneNumber.of("010-7900-7714"))
                        .role(Role.USER)
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

    @Test
    public void 회원가입을_테스트한다() throws Exception {
        //given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", "a79007714@gmail.com");
        jsonObject.put("password", "wnsdud123");
        jsonObject.put("name", "임준영");
        jsonObject.put("phoneNumber", "010-7900-7714");

        Member member = Member.builder()
                .name(Name.of("임준영"))
                .email(Email.of("a79007714@gmail.com"))
                .password(Password.of("wnsdud123"))
                .phoneNumber(PhoneNumber.of("010-7900-7714"))
                .build();

        when(memberSignUpService.doSignUp(any())).thenReturn(member);

        mockMvc.perform(post("/api/members")
                .content(jsonObject.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
