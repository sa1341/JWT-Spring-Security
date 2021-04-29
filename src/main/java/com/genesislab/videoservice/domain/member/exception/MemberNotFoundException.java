package com.genesislab.videoservice.domain.member.exception;

import com.genesislab.videoservice.global.error.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
