package com.genesislab.videoservice.domain.member.exception;

import com.genesislab.videoservice.global.error.exception.ErrorCode;
import com.genesislab.videoservice.global.error.exception.InvalidException;

public class PasswordNotMatchException extends InvalidException {

    public PasswordNotMatchException() {
        super(ErrorCode.PASSWORD_NOT_MATCHED);
    }
}
