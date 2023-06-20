package ru.skypro.lessons.springboot.springboot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class InfoController {

    @GetMapping("/appInfo")
    public String appInfo(@Value("${app.env}") String appInfo) {
        return appInfo;
    }
}
