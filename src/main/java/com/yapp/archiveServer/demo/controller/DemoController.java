package com.yapp.archiveServer.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/demo")
    public String demoGet() {
        return "hello, world!";
    }

    @GetMapping("/hot-reload")
    public String hotReload() {
        return "hotReload Okay";
    }

}
