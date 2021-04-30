package com.genesislab.videoservice.domain.member.dto;

import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.domain.model.Password;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;

@ToString(of = {"email"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignInRequest {

    @Valid
    private Email email;

    @Valid
    private Password password;
}
