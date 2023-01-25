package com.yapp.artie.domain.s3.controller;

import com.yapp.artie.domain.s3.dto.request.GetPresignedUrlRequestDto;
import com.yapp.artie.domain.s3.dto.response.GetPresignedUrlResponseDto;
import com.yapp.artie.domain.s3.dto.response.presignedUrlDataDto;
import com.yapp.artie.domain.s3.service.S3Service;
import com.yapp.artie.domain.user.service.UserService;
import com.yapp.artie.global.exception.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/s3")
@RestController
@RequiredArgsConstructor
public class S3Controller {

  private final UserService userService;
  private final S3Service s3Service;

  @Operation(summary = "이미지 업로드 URL 요청", description = "S3로 이미지를 업로드할 Presigned URL 발급 요청으로, 정상적으로 발급된 이미지에 대해서만 반환함. 각 Presigned URL은 약 3분간 유효함.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "S3 Presigned URL이 성공적으로 발급됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetPresignedUrlResponseDto.class))),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 입력",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "401",
          description = "인증 오류",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 회원",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  })
  @PostMapping("/url")
  public ResponseEntity<GetPresignedUrlResponseDto> generateArtworkPresignedUrl(
      Authentication authentication,
      @RequestBody @Valid GetPresignedUrlRequestDto getPresignedUrlRequestDto,
      @RequestParam(required = true, value = "id") Long postId) {
    Long userId = Long.parseLong(authentication.getName());
    userService.findUser(userId);
    AtomicInteger index = new AtomicInteger(1);
    List<presignedUrlDataDto> urlDataList = getPresignedUrlRequestDto.getImageNames()
        .stream().map(imageName -> s3Service.getPresignedUrl(imageName, postId,
            index.getAndIncrement())).filter(url -> url.isPresent()).filter(Optional::isPresent)
        .map(Optional::get).collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new GetPresignedUrlResponseDto(urlDataList));
  }
}
