package com.demo.album.service;

import com.demo.album.dto.UserChallengeDto;
import com.demo.album.entity.Challenge;
import com.demo.album.entity.User;
import com.demo.album.entity.UserChallenge;
import com.demo.album.repository.ChallengeRepository;
import com.demo.album.repository.UserChallengeRepository;
import com.demo.album.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChallengeService implements CommandLineRunner {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository, UserChallengeRepository userChallengeRepository, UserRepository userRepository) {
        this.challengeRepository = challengeRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.userRepository = userRepository;
    }

    // 모든 크루 모집글 조회
    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    // ID로 크루 모집글 상세 조회
    public Challenge getChallengeById(Long id) {
        return challengeRepository.findById(id).orElse(null);
    }

    // 크루 모집글에 이미지와 리뷰 업로드
    public void saveChallengeSubmission(Long id, MultipartFile file, String review, String uploadDate, User user) throws IOException {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid challenge ID"));

        // 이미지 저장을 위한 디렉토리 생성
        String uploadDir = "uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdir();
            if (!dirCreated) {
                throw new IOException("Failed to create upload directory");
            }
        }

        // 파일 이름을 UUID로 생성하여 저장
        String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String randomFileName = UUID.randomUUID().toString() + fileExtension;
        String filePath = uploadDir + randomFileName;
        File savedFile = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(savedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            throw new IOException("Failed to save the uploaded file", e);
        }

        // UserChallenge 객체 생성 및 저장
        UserChallenge userChallenge = new UserChallenge();
        userChallenge.setChallenge(challenge);
        userChallenge.setUser(user);
        userChallenge.setImage(filePath);  // 저장된 파일 경로 설정
        userChallenge.setReview(review);
        userChallenge.setUploadDate(uploadDate);
        userChallenge.setGroup(user.getGroup());

        userChallengeRepository.save(userChallenge); // UserChallenge 엔티티 저장
    }

    // 특정 사용자의 중복 없는 크루 모집글 목록 조회 (가장 최근의 uploadDate 기준)
    public List<UserChallengeDto> getDistinctChallengesByUser(Long groupId) {
        Map<Long, UserChallenge> latestChallenges = userChallengeRepository.findAllByGroup_Id(groupId).stream()
                .collect(Collectors.toMap(
                        userChallenge -> userChallenge.getChallenge().getId(),
                        userChallenge -> userChallenge,
                        (uc1, uc2) -> uc1.getUploadDate().compareTo(uc2.getUploadDate()) > 0 ? uc1 : uc2
                ));

        return latestChallenges.values().stream()
                .map(userChallenge -> new UserChallengeDto(
                        userChallenge.getChallenge().getId(),
                        userChallenge.getChallenge().getCategory(),
                        userChallenge.getChallenge().getTitle(),
                        userChallenge.getChallenge().getDday(),
                        userChallenge.getChallenge().getPoints(),
                        userChallenge.getChallenge().getPeriod(),
                        userChallenge.getChallenge().getLocation(),
                        userChallenge.getChallenge().getFee(),
                        userChallenge.getChallenge().getDescription(),
                        userChallenge.getUploadDate()  // 가장 최근의 Upload date 추가
                ))
                .collect(Collectors.toList());
    }


    // 특정 사용자의 그룹 ID 특정 크루 모집글에 대한 리뷰 조회
    public List<UserChallenge> getUserChallengesByChallengeIdAndGroup(Long challengeId, Long groupId) {
        return userChallengeRepository.findByChallengeIdAndGroup_Id(challengeId, groupId);
    }



    // 초기 데이터 추가
    @Override
    public void run(String... args) throws Exception {


        if (challengeRepository.count() == 0) {
            Challenge challenge1 = new Challenge("수지 지역 크루원 모집합니다", "가을 정기 달리기", 5, 135, "2024-10-18~2024-11-17", "풍덕천동", "무료", "수지 지역 정기 모임");
            Challenge challenge2 = new Challenge("강남 러너스하이 모집중", "강남 도산대로 무차별 달리기", 7, 260, "2024-10-18~2024-11-17", "도산대로", "무료", "강남 러너들 모집해요");
            Challenge challenge3 = new Challenge("칸예 런어웨이 크루원 대모집", "런어웨이 런어웨이", 15, 310, "2024-10-18~2024-11-17", "고양 종합운동장", "무료", "빨리 달리기 잘하시는분");

            challengeRepository.save(challenge1);
            challengeRepository.save(challenge2);
            challengeRepository.save(challenge3);
        }
    }
}

