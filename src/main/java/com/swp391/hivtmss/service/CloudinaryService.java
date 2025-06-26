package com.swp391.hivtmss.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadFile(MultipartFile multipartFile) throws IOException;

    void deleteFile(String imageUrl) throws IOException;
}
