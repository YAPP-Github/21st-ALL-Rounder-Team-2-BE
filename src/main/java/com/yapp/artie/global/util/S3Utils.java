package com.yapp.artie.global.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import java.util.Date;
import java.util.Optional;
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

  @Value("${cloud.aws.cloudfront.domain}")
  private String cdnDomain;

  private final AmazonS3Client amazonS3Client;

  public String generatePresignedUrl(String objectKey, long expirationMin) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest =
        new GeneratePresignedUrlRequest(bucketName, objectKey)
            .withMethod(HttpMethod.PUT)
            .withExpiration(convertExpirationMinIntoDate(expirationMin));
    generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL,
        CannedAccessControlList.Private.toString());
    return amazonS3Client.generatePresignedUrl(generatePresignedUrlRequest).toString();
  }

  public void deleteObject(String objectKey) {
    amazonS3Client.deleteObject(bucketName, objectKey);
  }

  public String getFullUri(String uri) {
    if (Optional.ofNullable(uri).isEmpty()) {
      return null;
    }
    return cdnDomain + uri;
  }

  private Date convertExpirationMinIntoDate(long expirationMin) {
    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += 1000 * 60 * expirationMin;
    expiration.setTime(expTimeMillis);
    return expiration;
  }

}
