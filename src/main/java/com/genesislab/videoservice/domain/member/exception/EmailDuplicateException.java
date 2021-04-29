package com.genesislab.videoservice.domain.member.exception;

import com.genesislab.videoservice.domain.model.Email;
import com.genesislab.videoservice.global.error.exception.ErrorCode;
import com.genesislab.videoservice.global.error.exception.InvalidException;

public class EmailDuplicateException extends InvalidException {

    public EmailDuplicateException(Email email) {
        super(email.getValue(), ErrorCode.INVALID_INPUT_VALUE);
    }
}
