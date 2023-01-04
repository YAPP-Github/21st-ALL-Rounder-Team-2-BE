package com.yapp.artie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ArtieServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(ArtieServerApplication.class, args);
  }
}
