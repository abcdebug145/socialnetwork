package com.project.socialnetwork.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletContext;

@Service
public class ContentDetectionService {
    private static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";
    private static final int DEFAULT_PORT = 5000;

    private static final Logger logger = LoggerFactory.getLogger(ContentDetectionService.class);

    private final ServletContext servletContext;

    public ContentDetectionService(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String getContent(String image) {
        String imagePath = this.servletContext.getRealPath("/resources/images/post/" + image);
        try (Socket socket = new Socket(DEFAULT_SERVER_ADDRESS, DEFAULT_PORT);
             OutputStream outputStream = socket.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(new File(imagePath));
             InputStream inputStream = socket.getInputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();

            socket.shutdownOutput();

            StringBuilder responseBuilder = new StringBuilder();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                responseBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
            }

            return responseBuilder.toString();

        } catch (Exception e) {
            logger.error("Error detecting content for image at path {}", imagePath, e);
        }
        return "";
    }
}
