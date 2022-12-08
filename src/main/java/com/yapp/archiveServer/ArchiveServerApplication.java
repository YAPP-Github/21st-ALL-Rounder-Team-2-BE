package com.yapp.archiveServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ArchiveServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArchiveServerApplication.class, args);
    }
}
