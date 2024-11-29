package com.demo.album.service;

import com.demo.album.dto.ApiResponseDto;
import com.demo.album.entity.Project;
import com.demo.album.entity.User;
import com.demo.album.repository.ProjectRepository;
import com.demo.album.repository.UserRepository;
import com.demo.album.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;
    public ApiResponseDto.ResponseDto createProject(String projectName, String location, MultipartFile file, String content, String updated_at) {
        try {
            String filePath = FileUploadUtil.saveFile("uploads/", file);

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> userInfo = userRepository.findByUsername(userDetails.getUsername());


            Project project = new Project(projectName, location, filePath, content, updated_at,userInfo.get().getGroup());
            projectRepository.save(project);

            return new ApiResponseDto.ResponseDto(200, "프로젝트 생성 완료되었습니다.", project);
        } catch (IOException e) {
            return new ApiResponseDto.ResponseDto(500, "파일 저장 중 오류가 발생했습니다: " + e.getMessage(), null);
        }
    }

    public ApiResponseDto.ResponseDto searchProjectID(Long projectId){
        Optional<Project> p = projectRepository.findById(projectId);
        if (p.isEmpty()) {
            return new ApiResponseDto.ResponseDto(404, "프로젝트를 찾을 수 없습니다.", null);
        }
        return new ApiResponseDto.ResponseDto(200, "프로젝트 조회 완료.",p.get());
    }


}
