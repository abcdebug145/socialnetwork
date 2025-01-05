package com.project.socialnetwork.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import jakarta.servlet.ServletContext;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    private final ServletContext servletContext;

    public ImageService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String saveUploadFile(MultipartFile file, String targetFolder) {
        String imgFileName = "";
        if (file.isEmpty()) {
            return "";
        }
        try {
            byte[] bytes = file.getBytes();
            String rootPath = this.servletContext.getRealPath("/resources/images");
            File dir = new File(rootPath + File.separator + targetFolder);
            if (!dir.exists())
                dir.mkdirs();
            // Create the file on server
            File serverFile = new File(dir.getAbsolutePath() + File.separator +
                    +System.currentTimeMillis() + "-" + file.getOriginalFilename());
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(serverFile));
            imgFileName = serverFile.getName();
            stream.write(bytes);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgFileName;
    }

    public void removeImage(String imageName, String targetFolder) {
        String imagePath = this.servletContext.getRealPath("/resources/images/" + targetFolder + "/" + imageName);
        try {
            File file = new File(imagePath);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
