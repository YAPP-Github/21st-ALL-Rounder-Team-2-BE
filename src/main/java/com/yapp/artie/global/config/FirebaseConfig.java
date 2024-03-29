package com.yapp.artie.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import java.io.FileInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FirebaseConfig {

  @Bean
  public FirebaseApp firebaseApp() throws IOException {
    log.info("Initializing Firebase.");
    FileInputStream serviceAccount =
        new FileInputStream("./firebase.json");

    FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build();

    FirebaseApp app = FirebaseApp.getApps()
        .stream()
        .filter(app1 -> app1.getName().equals(FirebaseApp.DEFAULT_APP_NAME))
        .findFirst()
        .orElseGet(() -> FirebaseApp.initializeApp(options));

    log.info("FirebaseApp initialized" + app.getName());
    return app;
  }


  @Bean
  public FirebaseAuth getFirebaseAuth() throws IOException {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(firebaseApp());
    return firebaseAuth;
  }
}