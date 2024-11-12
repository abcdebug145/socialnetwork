package com.project.socialnetwork.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.socialnetwork.service.VisualSearchClient;

import java.io.IOException;

@RestController
//@RequestMapping("/image-search")
public class ImageSearchController {
    @Autowired
    private VisualSearchClient visualSearchClient;

    @PostMapping("/search")
    public String searchImage(@RequestParam("image") MultipartFile image) {
        try {
            // Save the uploaded image temporarily
            String tempImagePath = "path/to/temp/dir/" + image.getOriginalFilename();
            image.transferTo(new java.io.File(tempImagePath));

            // Use VisualSearchClient to search for similar images
            return visualSearchClient.search(tempImagePath);

        } catch (IOException e) {
            e.printStackTrace();
            return "Error occurred while processing the image";
        }
    }
}