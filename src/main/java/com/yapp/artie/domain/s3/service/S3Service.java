package com.yapp.artie.domain.s3.service;

import com.yapp.artie.domain.s3.dto.response.presignedUrlDataDto;
import com.yapp.artie.global.util.S3Utils;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final S3Utils s3Utils;

  public Optional<presignedUrlDataDto> getPresignedUrl(String imageName, Long postId, int index) {
    try {
      String objectKey = String.format("artie/post/%d/%s_%d.%s", postId,
          Long.toString(new Date().getTime()), index,
          getExtension(imageName).orElseGet(() -> "jpg"));
      return Optional.of(
          new presignedUrlDataDto(s3Utils.generatePresignedUrl(objectKey, 3), imageName,
              objectKey));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  private Optional<String> getExtension(String filename) {
    return Optional.ofNullable(filename)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(filename.lastIndexOf(".") + 1));
  }
}
