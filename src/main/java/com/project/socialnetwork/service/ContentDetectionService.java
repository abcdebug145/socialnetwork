package com.project.socialnetwork.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletContext;

@Service
public class ContentDetectionService {
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private Environment environment;

    public String getContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);

            List<String> tags = new ArrayList<>();
            JsonNode tagsNode = rootNode.path("result").path("tags");

            for (JsonNode tagNode : tagsNode) {
                double confidence = tagNode.path("confidence").asDouble();
                if (confidence > 50.0) {
                    String tag = tagNode.path("tag").path("en").asText();
                    tags.add(tag);
                }
            }
            return "This post may be contains " + tags.toString();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getJsonResponse(String imageName) throws IOException {
        String apiKey = environment.getProperty("env.data.apiKey");
        String apiSecret = environment.getProperty("env.data.apiSecret");

        // Encode the credentials to Base64
        String credentialsToEncode = apiKey + ":" + apiSecret;
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        // Imagga endpoint for uploading image
        String endpointUrl = "https://api.imagga.com/v2/tags";

        // Path to your local image file
        String pathName = this.servletContext.getRealPath("/resources/images/post/" + imageName);
        File imageFile = new File(pathName);

        // Set up connection
        URL urlObject = new URL(endpointUrl);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        // Set the content type to multipart/form-data
        String boundary = "----WebKitFormBoundary" + System.currentTimeMillis();
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setDoOutput(true);

        // Write the file content to the output stream
        try (OutputStream outputStream = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8),
                        true)) {

            // Add the file part
            writer.append("--").append(boundary).append("\r\n");
            writer.append("Content-Disposition: form-data; name=\"image\"; filename=\"").append(imageFile.getName())
                    .append("\"\r\n");
            writer.append("Content-Type: ").append("image/jpeg").append("\r\n");
            writer.append("\r\n").flush();

            // Read file and write its content to output stream
            try (FileInputStream fileInputStream = new FileInputStream(imageFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }

            // End of multipart form data
            writer.append("\r\n").append("--").append(boundary).append("--").append("\r\n").flush();
        }

        // Get response code and response message
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        // Read the JSON response
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder jsonResponse = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                jsonResponse.append(inputLine);
            }

            System.out.println("Response: " + jsonResponse.toString());
            return jsonResponse.toString();
        } finally {
            connection.disconnect();
        }
    }
}
