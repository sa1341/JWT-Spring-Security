package com.genesislab.videoservice.domain.member.exception;

import com.genesislab.videoservice.global.error.exception.EntityNotFoundException;

public class EmailNotFoundException extends EntityNotFoundException {
    public EmailNotFoundException(String message) {
        super(message + "is not found");
    }
}
