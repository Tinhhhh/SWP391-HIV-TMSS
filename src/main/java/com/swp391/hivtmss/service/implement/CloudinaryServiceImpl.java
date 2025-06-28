package com.swp391.hivtmss.service.implement;

import com.cloudinary.Cloudinary;
import com.swp391.hivtmss.service.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new IOException("File is empty");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("public_id", UUID.randomUUID().toString());
        params.put("resource_type", "auto");

        return cloudinary.uploader()
                .upload(multipartFile.getBytes(), params)
                .get("url")
                .toString();
    }

    @Override
    public void deleteFile(String imageUrl) throws IOException {
        try {
            // Extract public ID from URL
            // Example URL: https://res.cloudinary.com/your-cloud-name/image/upload/v1234567890/public-id.jpg
            String[] urlParts = imageUrl.split("/");
            String fileName = urlParts[urlParts.length - 1];
            // Remove file extension and version number if present
            String publicId = fileName.substring(0, fileName.lastIndexOf("."));
            if (publicId.contains("v")) {
                publicId = publicId.substring(publicId.indexOf("v") + 8); // Skip "v" + 7 digits
            }

            // Set up deletion parameters
            Map<String, Object> params = new HashMap<>();
            params.put("resource_type", "image");

            // Perform the deletion
            Map result = cloudinary.uploader().destroy(publicId, params);

            // Check if deletion was successful
            String status = (String) result.get("result");
            if (!"ok".equals(status)) {
                throw new IOException("Failed to delete image from Cloudinary. Status: " + status);
            }
        } catch (Exception e) {
            throw new IOException("Error deleting file from Cloudinary: " + e.getMessage(), e);
        }

    }
}
