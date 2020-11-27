package ru.ineureka.patents.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    @GetMapping
    List<?> getNews() {
        return Collections.emptyList();
    }
}
