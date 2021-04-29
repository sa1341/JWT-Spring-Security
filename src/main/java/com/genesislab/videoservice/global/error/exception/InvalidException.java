package com.genesislab.videoservice.global.error.exception;

public class InvalidException extends BusinessException {

    public InvalidException(String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
