package com.shoppingo.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path:}")
    private String firebaseConfigPath;

    @Value("${firebase.config.json:}")
    private String firebaseConfigJson;

    @PostConstruct
    public void initialize() {
        try {
            List<FirebaseApp> apps = FirebaseApp.getApps();
            if (apps.isEmpty()) {
                InputStream serviceAccount;

                if (firebaseConfigJson != null && !firebaseConfigJson.trim().isEmpty()) {
                    // Load from JSON string (Environment Variable)
                    serviceAccount = new java.io.ByteArrayInputStream(firebaseConfigJson.getBytes());
                    System.out.println("Loading Firebase credentials from environment variable");
                } else if (firebaseConfigPath != null && !firebaseConfigPath.trim().isEmpty()) {
                    // Load from ClassPathResource
                    serviceAccount = new ClassPathResource(firebaseConfigPath).getInputStream();
                    System.out.println("Loading Firebase credentials from file: " + firebaseConfigPath);
                } else {
                    throw new IOException(
                            "No Firebase configuration found (neither JSON string nor file path provided)");
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Application has been initialized");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Firebase initialization failed: " + e.getMessage());
        }
    }
}
