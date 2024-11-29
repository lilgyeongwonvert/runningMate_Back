package com.demo.album.controller;

import com.demo.album.dto.ApiResponseDto;
import com.demo.album.service.ProjectService;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 사용자 이름 중복")
    })
    @ResponseBody
    public ApiResponseDto.ResponseDto createProject(
            @RequestParam("projectName") String projectName,
            @RequestParam("location") String location,
            @RequestParam("content") String content,
            @RequestParam("updated_at") String updatedAt,
            @RequestParam("url") MultipartFile file) throws IOException{



        ApiResponseDto.ResponseDto response = projectService.createProject(
                projectName, location, file, content,updatedAt
        );
        return response;

    }

    @GetMapping("/search/{projectId}")
    public ResponseEntity<?> search(@PathVariable("projectId") Long projectId) {
        ApiResponseDto.ResponseDto response = projectService.searchProjectID(projectId);
        return ResponseEntity.ok(response);
    }


}
