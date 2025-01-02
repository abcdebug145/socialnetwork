package com.project.socialnetwork.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletContext;

@Service
public class ContentDetectionService {
    @Autowired
    private ServletContext servletContext;

    public String getContent(String image) {
        String serverAddress = "127.0.0.1";
        String imagePath = this.servletContext.getRealPath("/resources/images/post/" + image);
        int port = 5000;
        try (Socket socket = new Socket(serverAddress, port);
                OutputStream outputStream = socket.getOutputStream();
                FileInputStream fileInputStream = new FileInputStream(new File(imagePath));
                InputStream inputStream = socket.getInputStream()) {

            // Đọc file ảnh thành byte[]
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
            e.printStackTrace();
        }
        return "";
    }

}
