package com.example.productservice.service;


import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@Service
public class GoogleStorageService {

    public static GoogleCredentials generateCredentials() throws IOException {
        String serviceAccountFilePath = System.getenv("SERVICE_ACCOUNT_FILE_PATH");
        if (serviceAccountFilePath == null) {
            throw new FileNotFoundException("Service account file path not provided.");
        }

        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream(serviceAccountFilePath))
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        return credentials;
    }



}
