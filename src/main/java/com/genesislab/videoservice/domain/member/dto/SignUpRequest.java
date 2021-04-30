package com.genesislab.videoservice.domain.member.dto;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.model.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;

@ToString(of = {"email", "name", "phoneNumber"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpRequest {

    @Valid
    private Email email;

    @Valid
    private Password password;

    @Valid
    private Name name;

    @Valid
    PhoneNumber phoneNumber;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .name(name)
                .password(password)
                .phoneNumber(phoneNumber)
                .role(Role.USER)
                .build();
    }
}
