package com.demo.album.service;

import com.demo.album.dto.ApiResponseDto.ResponseDto;
import com.demo.album.dto.GroupResponseDto;
import com.demo.album.dto.GroupResponseDto.UserInfoDto;
import com.demo.album.entity.Groups;
import com.demo.album.entity.User;
import com.demo.album.repository.GroupRepository;
import com.demo.album.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    /**
     * 그룹 생성
     *
     * @param groupName
     * @return 상태 코드와 메시지가 포함된 ResponseDto.
     * 성공 시 { "code": 200, "msg": "Group saved successfully" }를 반환합니다.
     * @throws RuntimeException 그룹 생성 실패 시 예외가 발생하며, 클라이언트는 에러 메시지를 확인해야 합니다.
     */
    public ResponseDto create(String groupName) {
        try {
            // 1. 사용자 정보 가져오기
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> userInfo = userRepository.findByUsername(userDetails.getUsername());

            // 2. 사용자 정보 확인
            if (userInfo.isEmpty()) return new ResponseDto(404, "User not found");

            // 3. 이미 등록된 그룹이 있는지 확인 (없으면 그룹 생성)
            if (userInfo.get().getGroup() == null) {
                // 4. 그룹 생성
                Groups groups = Groups.builder()
                        .groupName(groupName)
                        .build();
                Groups groupInfo = groupRepository.save(groups);

                // 5. group Id 값 세팅
                User user = User.builder()
                        .userId(userInfo.get().getUserId())
                        .username(userInfo.get().getUsername())
                        .email(userInfo.get().getEmail())
                        .nickname(userInfo.get().getNickname())
                        .password(userInfo.get().getPassword())
                        .group(groupInfo)
                        .build();
                userRepository.save(user);

                return new ResponseDto(200, "Group saved successfully", groupInfo.getGroupName());
            } else return new ResponseDto(400, "Group already exists for this user");
        } catch (Exception e) {
            throw new RuntimeException("Failed to create group: " + e.getMessage());
        }
    }

    /**
     * 크루 구성원 추가
     *
     * @param userId
     * @return
     */
    public ResponseDto add(String userId) {
        try {
            // 1. 사용자 정보 가져오기
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> userInfo = userRepository.findByUsername(userDetails.getUsername());

            // 2. 사용자 정보 확인
            if (userInfo.isEmpty()) return new ResponseDto(404, "User not found");

            // 3. 그룹이 이미 생성된 사용자만 가족 구성원 추가 가능
            if (userInfo.get().getGroup() != null) {
                Optional<User> memberInfo = userRepository.findById(Long.valueOf(userId));
                User user = User.builder()
                        .userId(memberInfo.get().getUserId())
                        .username(memberInfo.get().getUsername())
                        .password(memberInfo.get().getPassword())
                        .nickname(memberInfo.get().getNickname())
                        .email(memberInfo.get().getEmail())
                        .group(userInfo.get().getGroup())
                        .build();
                userRepository.save(user);
                return new ResponseDto(200, "Family member added successfully");
            } else return new ResponseDto(400, "No group is registered for this user. Cannot add family members.");
        } catch (Exception e) {
            return new ResponseDto(500, "Failed to add family member: " + e.getMessage());
        }
    }

    /**
     * 크루 구성원 조회
<<<<<<< HEAD
     * @param username
     * @return 그룹에 등록 가능한 사용자 여부
     */
    public ResponseDto search(String userName) {
        // 1. 사용자 정보 확인
        try {
            Optional<User> userInfo = userRepository.findByUsername(userName);

            if (userInfo.isPresent()) {
                // 2. 그룹에 등록된 사용자인지 확인
                if (userInfo.get().getGroup() == null) {
                    // 등록된 그룹이 없는 사용자인 경우
                    UserInfoDto userInfoDto = UserInfoDto.builder()
                            .userId(userInfo.get().getUserId())
                            .email(userInfo.get().getEmail())
                            .nickname(userInfo.get().getNickname())
                            .username(userInfo.get().getUsername())
                            .build();
                    return new ResponseDto(200, "등록 가능한 사용자입니다.", userInfoDto);
                } else {
                    // 이미 등록된 그룹이 있는 사용자인 경우
                    return new ResponseDto(400, "이미 등록된 그룹이 있는 사용자입니다.");
                }
            } else {
                return new ResponseDto(404, "사용자 정보를 찾을 수 없습니다.");
            }
        } catch (NumberFormatException e) {
            // userId가 올바른 형식의 숫자가 아닌 경우
            return new ResponseDto(400, "잘못된 사용자 ID 형식입니다.");
        } catch (Exception e) {
            // 그 외의 모든 예외 처리
            return new ResponseDto(500, "가족 구성원 조회 error: " + e.getMessage());
        }
    }

    /**
     * 가족 구성원 삭제
     * @param userId
     * @return 삭제 완료 msg
     */
    public ResponseDto remove(Long userId) {
        try {
            // 1. 사용자 정보 조회
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> userInfo = userRepository.findByUsername(userDetails.getUsername());

            // 2. 삭제할 사용자 정보 조회
            Optional<User> deleteUserInfo = userRepository.findById(userId);

            if (deleteUserInfo.isPresent()) {
                // 2. 내가 속해 있는 그룹 사용자만 해제 가능
                if (userInfo.get().getGroup().getId() == deleteUserInfo.get().getGroup().getId()) {
                    // 3. 그룹 관계 해제
                    User updatedUser = deleteUserInfo.get().toBuilder()
                            .group(null)
                            .build();
                    userRepository.save(updatedUser);
                    return new ResponseDto(200, "가족 구성원의 그룹 관계를 성공적으로 해제했습니다.");
                } else return new ResponseDto(403, "같은 그룹의 사용자만 삭제할 수 있습니다.");
            } else {
                return new ResponseDto(404, "사용자 정보를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return new ResponseDto(500, "가족 구성원 삭제 error: " + e.getMessage());
        }
    }

//    public GroupGanzi createGroup(String groupName) {
//        GroupGanzi groupGanzi = new GroupGanzi(groupName);
//        return groupRepository.save(groupGanzi);
//    }
}