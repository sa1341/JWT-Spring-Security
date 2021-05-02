package com.genesislab.videoservice.domain.member.dto;

import com.genesislab.videoservice.domain.model.Name;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberUpdateReq {
    @Valid
    private Name name;
}
