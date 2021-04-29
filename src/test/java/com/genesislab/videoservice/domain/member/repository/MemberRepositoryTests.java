package com.genesislab.videoservice.domain.member.repository;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.entity.QMember;
import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.domain.model.Name;
import com.genesislab.videoservice.domain.model.Password;
import com.genesislab.videoservice.domain.model.PhoneNumber;
import com.genesislab.videoservice.global.error.exception.BusinessException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JPAQueryFactory queryFactory;

    @BeforeEach
    public void setUp() {
        Member member = Member.builder()
                .email(Email.of("a79007714@gmail.com"))
                .password(Password.of("wnsdud@123"))
                .name(Name.of("임준영"))
                .phoneNumber(PhoneNumber.of("010-7900-7714"))
                .build();

        memberRepository.save(member);
    }

    @Test
    public void 회원을_저장한다() throws BusinessException {
        //given
        String email = "a79007714@gmail.com";

        //when
        Optional<Member> optionalMember = memberRepository.findByEmail(Email.of(email));

        if (optionalMember.isPresent()) {
            Member findMember = optionalMember.get();
            System.out.println(findMember);
            //then
            assertThat(findMember.getEmail().getValue()).isEqualTo(email);
        }
    }

    @Test
    public void 회원을_조회한다() throws Exception {
        //given
        String email = "a879007714@gmail.com";

        //when
        Member member = queryFactory.selectFrom(QMember.member)
                .where(QMember.member.email.value.eq("a79007714@gmail.com"))
                .fetchOne();

        System.out.println(member);

        //then
        assertThat(member.getName().getValue()).isEqualTo("임준영");
     }
}
