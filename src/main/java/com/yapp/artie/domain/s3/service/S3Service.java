package com.yapp.artie.domain.s3.service;

import com.yapp.artie.domain.s3.dto.response.presignedUrlDataDto;
import com.yapp.artie.global.util.S3Utils;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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

  public void deleteObject(String objectUri) {
    try {
      s3Utils.deleteObject(objectUri);
    } catch (Exception e) {
      log.error("Error occurred while delete object(URI:{})", objectUri, e);
    }
  }

  private Optional<String> getExtension(String filename) {
    return Optional.ofNullable(filename)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(filename.lastIndexOf(".") + 1));
  }
}
