package com.yapp.archiveServer.global.error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    //Auth
    AUTH_SERVER_ERROR(401, "A001", "인증 서버와의 연동에 실패했습니다."),
    AUTH_INVALID_USERINFO(401, "A002", "유저정보가 올바르지 않습니다."),
    AUTH_ACCESS_TOKEN_EXPIRED(401, "A003", "액세스 토큰이 만료되었습니다."),

    // User
    USER_TOKEN_EXPIRED(401, "U001", "토큰이 만료되었습니다. 재발급이 필요합니다."),
    USER_TOKEN_ERROR(401, "U002", "JWT 토큰 파싱에 실패했습니다."),
    USER_REFRESH_TOKEN_EXPIRED(401, "U003", "JWT 리프레시 토큰이 만료되었습니다. 재로그인이 필요합니다."),
    USER_REFRESH_ERROR(401, "U004", "유효하지 않은 JWT 리프레시 토큰입니다. 재로그인이 필요합니다."),
    USER_NOT_FOUND(404, "U005", "회원을 찾을 수 없습니다."),
    USER_FORBIDDEN(403, "U006", "잘못된 접근입니다."),
    USER_ALREADY_LOGOUT_TOKEN(200, "U007", "이미 로그아웃된 유저입니다."),
    USER_ALREADY_EXISTS(409, "U008", "이미 존재하는 유저입니다.");

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final int status, final String code, final String message) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}