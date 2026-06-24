package com.batch22bd.BackEnd.Service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    image.getBytes(),
                    Map.of("folder", "foods")
            );
            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to upload image to Cloudinary", e);
        }
    }
}
