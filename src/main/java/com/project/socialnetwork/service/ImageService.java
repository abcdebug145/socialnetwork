package com.project.socialnetwork.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletContext;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private final ServletContext servletContext;

    public ImageService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String saveUploadFile(MultipartFile file, String targetFolder) {
        if (file == null || file.isEmpty()) {
            return "";
        }
        String imgFileName = "";
        try {
            byte[] bytes = file.getBytes();
            String rootPath = this.servletContext.getRealPath("/resources/images");
            File dir = new File(rootPath + File.separator + targetFolder);
            if (!dir.exists() && !dir.mkdirs()) {
                logger.warn("Failed to create directory for image upload at path: {}", dir.getAbsolutePath());
            }

            File serverFile = new File(dir.getAbsolutePath() + File.separator +
                    System.currentTimeMillis() + "-" + file.getOriginalFilename());
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
                stream.write(bytes);
            }
            imgFileName = serverFile.getName();
        } catch (IOException e) {
            logger.error("Error saving uploaded file", e);
            return "";
        }
        return imgFileName;
    }

    public void removeImage(String imageName, String targetFolder) {
        String imagePath = this.servletContext.getRealPath("/resources/images/" + targetFolder + "/" + imageName);
        try {
            File file = new File(imagePath);
            if (file.exists() && !file.delete()) {
                logger.warn("Failed to delete image at path: {}", imagePath);
            }
        } catch (Exception e) {
            logger.error("Error deleting image at path: {}", imagePath, e);
        }
    }
}
