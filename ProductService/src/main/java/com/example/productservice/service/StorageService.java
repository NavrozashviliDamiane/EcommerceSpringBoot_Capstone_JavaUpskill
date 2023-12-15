package com.example.productservice.service;

import com.example.productservice.entity.ImageData;
import com.example.productservice.repository.StorageRepository;
import com.example.productservice.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.Optional;
@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;

    public void delete(ImageData image) {
        if (image != null) {
            Long imageId = image.getId();
            repository.deleteById(imageId);
        }
    }
    public ImageData saveOrUpdateImage(MultipartFile imageFile) throws IOException {
        // Convert the MultipartFile to byte[] (assuming you have an ImageUtils class)
        byte[] imageDataBytes = ImageUtils.compressImage(imageFile.getBytes());

        // Check if an image with the same name already exists in the repository
        Optional<ImageData> existingImageData = repository.findByName(imageFile.getOriginalFilename());

        ImageData imageData;
        if (existingImageData.isPresent()) {
            // If the image exists, update its data
            imageData = existingImageData.get();
            imageData.setImageData(imageDataBytes);
            imageData.setType(imageFile.getContentType());
        } else {
            // If the image doesn't exist, create a new one
            imageData = ImageData.builder()
                    .name(imageFile.getOriginalFilename())
                    .type(imageFile.getContentType())
                    .imageData(imageDataBytes)
                    .build();
        }

        // Save or update the image data in the repository
        return repository.save(imageData);
    }

}
