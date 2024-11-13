package com.project.socialnetwork.controller.client;

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
import java.util.Base64;

public class Test {
    public static void main(String[] args) throws IOException {
        String apiKey = "acc_50c2a40d27ff435";
        String apiSecret = "c99e88532b756ce236649f1543c5a0ad";

        // Encode the credentials to Base64
        String credentialsToEncode = apiKey + ":" + apiSecret;
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        // Imagga endpoint for uploading image
        String endpointUrl = "https://api.imagga.com/v2/tags";

        // Path to your local image file
        File imageFile = new File(
                "C:\\Users\\scamq\\OneDrive\\Desktop\\pbl\\socialnetwork\\src\\main\\webapp\\resources\\images\\post\\1f76841da7121d20a908e33f0c8df2a1.jpg");

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
        }

        connection.disconnect();
    }
}
