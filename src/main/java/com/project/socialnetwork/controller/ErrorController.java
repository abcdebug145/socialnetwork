package com.project.socialnetwork.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        return "redirect:/page-not-found";
    }
}
