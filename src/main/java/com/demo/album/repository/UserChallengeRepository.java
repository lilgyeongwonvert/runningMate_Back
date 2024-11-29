package com.demo.album.repository;

import com.demo.album.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    // 특정 사용자에 대한 도전 과제 목록 조회
    List<UserChallenge> findByUserUserId(Long userId);

    // 특정 사용자 그룹 ID 기준으로 도전 과제 목록 조회
    List<UserChallenge> findAllByGroup_Id(Long groupId);

    // 특정 도전 과제에 대한 모든 리뷰 조회
    List<UserChallenge> findByChallengeId(Long challengeId);

    // 특정 사용자와 특정 도전 과제에 대한 리뷰 조회
    List<UserChallenge> findByChallengeIdAndGroup_Id(Long challengeId, Long groupId);
}
