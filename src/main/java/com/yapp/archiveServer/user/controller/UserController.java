package com.yapp.archiveServer.user.controller;

import com.yapp.archiveServer.global.exception.CustomException;
import com.yapp.archiveServer.user.domain.User;
import com.yapp.archiveServer.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.yapp.archiveServer.global.exception.ErrorCode.USER_NOT_FOUND;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "유저 생성", description = "Firebase를 통해 생성한 UID 기반 유저 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "유저가 성공적으로 생성됨"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 UID"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/{uid}")
    public ResponseEntity<Long> createUserByUid(@PathVariable(name = "uid") String uid) {
        User newUser = userService.findByUid(uid).orElseThrow(() -> new CustomException(USER_NOT_FOUND, "user UID = " + uid));
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser.getId());
    }
}
