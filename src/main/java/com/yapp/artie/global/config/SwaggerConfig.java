package com.yapp.artie.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(title = "YAPP 21st ALl2 API 명세서",
        description = "YAPP 21st ALl2 API 명세서 입니다.",
        version = "v1"))
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi customTestOpenAPi() {
    // /test 로 시작하는 API 들을 테스트 관련 API 로 그룹핑
    String[] paths = {"/test/**"};

    return GroupedOpenApi
        .builder()
        .group("테스트 관련 API")
        .pathsToMatch(paths)
        .addOpenApiCustomiser(buildSecurityOpenApi()).build();
  }

  @Bean
  public GroupedOpenApi userOpenApi() {
    String[] paths = {"/user/**"};

    return GroupedOpenApi
        .builder()
        .group("유저 API")
        .pathsToMatch(paths)
        .addOpenApiCustomiser(buildSecurityOpenApi()).build();
  }

  @Bean
  public GroupedOpenApi categoryOpenAPi() {
    String[] paths = {"/category/**"};

    return GroupedOpenApi
        .builder()
        .group("카테고리 API")
        .pathsToMatch(paths)
        .addOpenApiCustomiser(buildSecurityOpenApi()).build();
  }

  @Bean
  public GroupedOpenApi postOpenAPi() {
    String[] paths = {"/post/**"};

    return GroupedOpenApi
        .builder()
        .group("전시 API")
        .pathsToMatch(paths)
        .addOpenApiCustomiser(buildSecurityOpenApi()).build();
  }

  @Bean
  public GroupedOpenApi artworkApi() {
    String[] paths = {"/artwork/**"};

    return GroupedOpenApi
        .builder()
        .group("작품 API")
        .pathsToMatch(paths)
        .addOpenApiCustomiser(buildSecurityOpenApi()).build();
  }

  @Bean
  public GroupedOpenApi noticeApi() {
    String[] paths = {"/notice/**"};

    return GroupedOpenApi
        .builder()
        .group("공지사항 API")
        .pathsToMatch(paths)
        .addOpenApiCustomiser(buildSecurityOpenApi()).build();
  }

  @Bean
  public GroupedOpenApi s3Api() {
    String[] paths = {"/s3/**"};

    return GroupedOpenApi
        .builder()
        .group("S3(이미지) API")
        .pathsToMatch(paths)
        .addOpenApiCustomiser(buildSecurityOpenApi()).build();
  }

  public OpenApiCustomiser buildSecurityOpenApi() {
    // jwt token 을 한번 설정하면 header 에 값을 넣어주는 코드
    return OpenApi -> OpenApi.addSecurityItem(new SecurityRequirement().addList("jwt token"))
        .getComponents().addSecuritySchemes("jwt token", new SecurityScheme()
            .name("Authorization")
            .type(SecurityScheme.Type.HTTP)
            .in(SecurityScheme.In.HEADER)
            .bearerFormat("JWT")
            .scheme("bearer"));
  }

}
