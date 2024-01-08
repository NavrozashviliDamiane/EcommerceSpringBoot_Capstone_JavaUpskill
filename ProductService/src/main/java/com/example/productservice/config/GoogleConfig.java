package com.example.productservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Configuration
public class GoogleConfig {
    @Bean
    public GoogleCredentials googleCredentials() throws IOException {




       // return credentials;
        GoogleCredentials googleCredentials = GoogleCredentials.newBuilder().build();
        return googleCredentials;
    }

}
