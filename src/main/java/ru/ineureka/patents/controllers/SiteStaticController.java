package ru.ineureka.patents.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SiteStaticController {

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }
}
