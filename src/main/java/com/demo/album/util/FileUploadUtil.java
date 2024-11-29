package com.demo.album.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FileUploadUtil {

    public static String saveFile(String uploadDir, MultipartFile file) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdirs();
            if (!dirCreated) {
                throw new IOException("Failed to create upload directory");
            }
        }

        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String randomFileName = UUID.randomUUID().toString() + fileExtension;
        String filePath = uploadDir + randomFileName;
        File savedFile = new File(filePath);

        try (FileOutputStream fos = new FileOutputStream(savedFile)) {
            fos.write(file.getBytes());
        }
        return filePath;
    }
}
