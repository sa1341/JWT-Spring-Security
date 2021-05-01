package com.genesislab.videoservice.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "Invalid input value"),
    METHOD_NOT_ALLOWED(405, "Invalid input value"),
    HANDLE_ACCESS_DENIED(403, "Access is denied"),
    ENTITY_NOT_FOUND(400, "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),

    // Member
    EMAIL_DUPLICATION(400, "Email is duplicated"),
    PASSWORD_NOT_MATCHED(400, "Password is not matched"),
    LOGIN_INPUT_INVALID(400, "Login Input is invalid"),
    REFRESH_TOKEN_EXPIRED(401, "RefreshToken is invalid");

    private final int status;
    private final String message;

    ErrorCode(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
