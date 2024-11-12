package com.project.socialnetwork.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.nio.file.Files;

@Service
public class VisualSearchClient {
    public String search(String image) {
        try {
            File pics = new File("src/main/resources/static/images/" + image);
            URL url = new URL("192.168.56.1:8088/analyze");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "multipart/form-data");
            con.setDoOutput(true);
            try {
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                Files.copy(pics.toPath(), out);
                out.flush();
                out.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = in.readLine()) != null) {
                    response.append(responseLine);
                }
                in.close();
                return response.toString();
            } catch (Exception e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
