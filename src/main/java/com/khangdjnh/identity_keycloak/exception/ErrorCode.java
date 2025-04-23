package com.khangdjnh.identity_keycloak.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    EXISTED_USER(10001, "User already existed", HttpStatus.CONFLICT),
    USER_NOT_FOUND(10002, "User not found", HttpStatus.NOT_FOUND),
    INVALID_USERNAME_OR_PASSWORD(10003, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    INVALID_USERNAME(10004, "Invalid username", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(10005, "Invalid password", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(10006, "Invalid token", HttpStatus.UNAUTHORIZED),
    UNAUTHENTICATED(10007, "User is not authenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(10008, "User is not authorized", HttpStatus.FORBIDDEN),
    INVALID_KEY(10009, "Invalid key", HttpStatus.BAD_REQUEST),
    ;

    int code;
    String message;
    HttpStatusCode httpStatusCode;
    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }
}
