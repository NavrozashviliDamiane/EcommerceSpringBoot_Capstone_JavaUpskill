package com.example.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithImagesDTO {
    private Long id;
    private String name;
    private String description;
    private ImageDataDTO imageData;

}

