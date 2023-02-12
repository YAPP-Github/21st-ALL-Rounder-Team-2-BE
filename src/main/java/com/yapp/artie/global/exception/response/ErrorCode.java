package com.yapp.artie.global.exception.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

  // Common
  INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
  METHOD_NOT_ALLOWED(405, "C002", "허용하지 않는 HTTP 메서드입니다."),
  ENTITY_NOT_FOUND(400, "C003", "엔티티를 찾을 수 없습니다."),
  INTERNAL_SERVER_ERROR(500, "C004", "서버 오류"),
  INVALID_TYPE_VALUE(400, "C005", "잘못된 타입의 값입니다."),
  HANDLE_ACCESS_DENIED(403, "C006", "접근이 거부됐습니다."),

  // Firebase Authentication
  FIREBASE_SERVER_ERROR(401, "F001", "인증 서버와의 연동에 실패했습니다."),
  FIREBASE_ACCESS_TOKEN_EXPIRED(401, "F002", "액세스 토큰이 만료되었습니다."),
  FIREBASE_TOKEN_NOT_EXISTS(401, "F003", "유효한 토큰이 존재하지 않습니다."),
  FIREBASE_INVALID_TOKEN(401, "F004", "JWT 토큰 파싱에 실패했습니다."),
  FIREBASE_REVOKED_TOKEN(401, "F005", "취소된 토큰입니다."),

  // User
  USER_NOT_FOUND(404, "U001", "회원을 찾을 수 없습니다."),
  USER_ALREADY_EXISTS(409, "U002", "이미 존재하는 유저입니다."),

  // Category
  CATEGORY_NOT_FOUND(404, "CA001", "카테고리가 존재하지 않습니다."),
  CATEGORY_ALREADY_EXISTS(409, "CA002", "이미 존재하는 카테고리입니다."),
  CATEGORY_NOT_OWNER(403, "CA003", "자신의 카테고리만 접근할 수 있습니다."),
  CATEGORY_EXCEEDED_COUNT(409, "CA004", "카테고리 최대 생성 갯수 5개를 초과했습니다."),
  CATEGORY_WRONG_CHANGE_LENGTH(400, "CA005", "수정할 카테고리의 개수는 원본의 개수와 같아야 합니다. "),

  // Exhibit
  EXHIBIT_NOT_FOUND(404, "E001", "전시가 존재하지 않습니다."),
  EXHIBIT_NOT_OWNER(403, "E002", "자신의 전시 정보만 접근할 수 있습니다."),
  EXHIBIT_ALREADY_PUBLISHED(409, "E003", "이미 발행된 전시입니다."),

  // Tag
  TAG_NOT_FOUND(404, "TAG001", "태그가 존재하지 않습니다."),
  TAG_ALREADY_EXISTS(409, "TAG002", "이미 존재하는 태그입니다."),

  // Artwork
  ARTWORK_NOT_OWNER(403, "AW001", "자신의 작품 정보만 접근할 수 있습니다."),
  ARTWORK_NOT_FOUND(404, "AW002", "작품이 존재하지 않습니다."),

  // S3
  S3_SERVICE_ERROR(500, "S3001", "AmazonServiceException 에러가 발생하였습니다."),
  S3_SDK_ERROR(500, "S3002", "SdkClientException 에러가 발생하였습니다."),

  // Notice
  NOTICE_NOT_FOUND(404, "N001", "공지사항이 존재하지 않습니다.");

  private final String code;
  private final String message;
  private final int status;

  ErrorCode(final int status, final String code, final String message) {
    this.code = code;
    this.message = message;
    this.status = status;
  }
}