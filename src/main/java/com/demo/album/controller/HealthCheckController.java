package com.demo.album.controller;

import com.demo.album.dto.ApiResponseDto;
import com.demo.album.dto.ApiUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/healthCheck")
    public ResponseEntity<?> healthCheck(){
        return ResponseEntity.ok(new ApiUtils<>(true,"success"));
    }
}
