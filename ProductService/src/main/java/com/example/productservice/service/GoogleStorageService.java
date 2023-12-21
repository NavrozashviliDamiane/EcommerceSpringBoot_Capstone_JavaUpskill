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
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new FileInputStream("C:\\Users\\Admin\\Desktop\\CapstoneProject\\JavaPage_Ecommerce\\ProductService\\src\\main\\resources\\key.json"))
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        return credentials;

    }



}
