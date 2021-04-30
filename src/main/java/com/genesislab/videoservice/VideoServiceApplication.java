package com.genesislab.videoservice;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.member.repository.MemberRepository;
import com.genesislab.videoservice.domain.member.service.MemberSignUpService;
import com.genesislab.videoservice.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@SpringBootApplication
public class VideoServiceApplication implements CommandLineRunner {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(VideoServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Member plainUser = Member.builder()
								 .email(Email.of("a79007714@gmail.com"))
								 .password(Password.of(passwordEncoder.encode("wnsdud2")))
								 .name(Name.of("임준영"))
								 .phoneNumber(PhoneNumber.of("010-7900-7714"))
								 .role(Role.USER)
								 .build();

		Member adminUser = Member.builder()
				.email(Email.of("syn7714@naver.com"))
				.password(Password.of(passwordEncoder.encode("wnsdud2tp")))
				.name(Name.of("임성희"))
				.phoneNumber(PhoneNumber.of("010-9869-7714"))
				.role(Role.ADMIN)
				.build();

		memberRepository.save(plainUser);
		memberRepository.save(adminUser);
	}
}
