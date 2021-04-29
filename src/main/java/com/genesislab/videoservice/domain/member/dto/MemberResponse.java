package com.genesislab.videoservice.domain.member.dto;

import com.genesislab.videoservice.domain.member.entity.Member;
import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.domain.model.Name;
import com.genesislab.videoservice.domain.model.PhoneNumber;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private Email email;
    private Name name;
    private PhoneNumber phoneNumber;

    public MemberResponse(final Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.phoneNumber = member.getPhoneNumber();
    }
}
