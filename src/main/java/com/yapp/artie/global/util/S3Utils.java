package com.yapp.artie.global.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Utils {

  @Value("${cloud.aws.s3.bucket}")
  private String bucketName;

  private final AmazonS3Client amazonS3Client;


  public String generatePresignedUrl(String objectKey, long expirationMin) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucketName, objectKey)
            .withMethod(HttpMethod.GET)
            .withExpiration(convertExpirationMinIntoDate(expirationMin));
    return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
  }

  private Date convertExpirationMinIntoDate(long expirationMin) {
    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += 1000 * 60 * expirationMin;
    expiration.setTime(expTimeMillis);
    return expiration;
  }

}
