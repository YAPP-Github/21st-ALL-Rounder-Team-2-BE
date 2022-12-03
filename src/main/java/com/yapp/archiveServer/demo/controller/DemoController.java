package com.yapp.archiveServer.demo.controller;

import com.yapp.archiveServer.demo.dto.DemoSignupRequestDto;
import com.yapp.archiveServer.demo.service.DemoService;
import com.yapp.archiveServer.global.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/demo")
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<String> demoGet() {
        return ApiResponse.createSuccess("hello, world");
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
        public ApiResponse<String> signup(@Valid @RequestBody DemoSignupRequestDto dto) {
            return ApiResponse.createSuccess(demoService.join(dto));
    }


}
