package com.demo.album.controller;

import com.demo.album.dto.ApiResponseDto;
import com.demo.album.entity.Challenge;
import com.demo.album.entity.User;
import com.demo.album.entity.UserChallenge;
import com.demo.album.service.ChallengeService;
import com.demo.album.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ChallengeController {

    private final ChallengeService challengeService;
    private final UserService userService;

    public ChallengeController(ChallengeService challengeService, UserService userService) {
        this.challengeService = challengeService;
        this.userService = userService;

    }

    // 모든 크루 모집글 조회
    @GetMapping("/challenges")
    public List<Challenge> getAllChallenges() {
        return challengeService.getAllChallenges();
    }

    // 특정 크루 모집글 상세 조회
    @GetMapping("/challenges/{id}")
    public ResponseEntity<Challenge> getChallengeById(@PathVariable Long id) {
        Challenge challenge = challengeService.getChallengeById(id);
        return challenge != null ? ResponseEntity.ok(challenge) : ResponseEntity.notFound().build();
    }

    // 크루 모집글에 이미지와 리뷰 업로드
    @PostMapping("/challenges/{id}/upload")
    public ResponseEntity<String> uploadChallenge(
            @PathVariable("id") Long id,
            @RequestParam("image") MultipartFile image,
            @RequestParam("review") String review,
            @RequestParam("uploadDate") String uploadDate,
            Principal principal
    ) {
        try {
            String username = principal.getName();
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

            challengeService.saveChallengeSubmission(id, image, review, uploadDate, user);
            return ResponseEntity.ok("Challenge uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload challenge: " + e.getMessage());
        }
    }

    // 특정 사용자의 중복 없는 크루 모집글 목록 조회
    @GetMapping("/user/challenges")
    public ApiResponseDto.ResponseDto getUserDistinctChallenges(Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        if (user.getGroup() == null) {
            return new ApiResponseDto.ResponseDto(
                    404,
                    "유저가 속한 그룹이 존재하지 않습니다.",
                    user // 데이터가 없으므로 null 반환
            );
        }
        return new ApiResponseDto.ResponseDto(200, "사용자 도전과제 목록 조회 완료", challengeService.getDistinctChallengesByUser(user.getGroup().getId()));
    }

    // 특정 크루 모집글의 리뷰 목록 조회 (로그인한 사용자의 리뷰만 반환)
    @GetMapping("/user/challenges/{challengeId}")
    public List<UserChallenge> getUserChallengeReviews(
            @PathVariable("challengeId") Long challengeId, Principal principal) {
        String username = principal.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // 해당 크루 모집글와 로그인한 사용자 그룹 ID의 리뷰만 반환
        return challengeService.getUserChallengesByChallengeIdAndGroup(challengeId, user.getGroup().getId());
    }
}
