package com.demo.album.service;

import com.demo.album.dto.*;
import com.demo.album.dto.ApiResponseDto.ResponseDto;
import com.demo.album.entity.Groups;
import com.demo.album.entity.Project;
import com.demo.album.entity.User;
import com.demo.album.repository.GroupRepository;
import com.demo.album.repository.ProjectRepository;
import com.demo.album.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjectRepository projectRepository;
    private final GroupRepository groupRepository;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,ProjectRepository projectRepository,GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.projectRepository = projectRepository;
        this.groupRepository = groupRepository;
    }

    public ResponseDto registerUser(String username, String password, String nickname, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(username, encodedPassword, nickname, email);
        userRepository.save(user);
        return new ResponseDto(200, "회원가입 완료되었습니다.", user);
    }


    public ApiResponseDto.ResponseDto search(Long userId) {
        // User 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 유저가 속한 그룹
        Groups group = user.getGroup();

        if (group == null) {
            return new ApiResponseDto.ResponseDto(
                    404,
                    "유저가 속한 그룹이 존재하지 않습니다.",
                    user // 데이터가 없으므로 null 반환
            );
        }

        // 유저의 프로젝트 리스트 조회
        List<ProjectDto> projectList = projectRepository.findAllByGroup_Id(group.getId())
                .stream()
                .map(project -> new ProjectDto(
                        project.getProjectId(),
                        project.getProjectName(),
                        project.getUrl(),
                        project.getUpdated_at()
                ))
                .collect(Collectors.toList());

        // 유저가 속한 그룹의 멤버 조회
        List<GroupMemberDto> groupMembers = userRepository.findAllByGroup_Id(group.getId())
                .stream()
                .map(member -> new GroupMemberDto(
                        member.getUsername(),
                        member.getUserId(),
                        member.getNickname()
                ))
                .collect(Collectors.toList());

        // 응답 데이터 구성
        UserResponseDto userResponseDto = new UserResponseDto(
                user.getPoint(),
                user.getUserId(),
                user.getNickname(),
                group.getGroupName(),
                projectList,
                groupMembers
        );

        return new ApiResponseDto.ResponseDto(201, "조회가 완료되었습니다.", userResponseDto);
    }

    public Optional<User> authenticateUser(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
    public User updateUser(String username, String newPassword, String newNickname, String newEmail, String newUsername) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        if (newPassword != null && !newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        if (newNickname != null && !newNickname.isEmpty()) {
            user.setNickname(newNickname);
        }

        if (newEmail != null && !newEmail.isEmpty()) {
            user.setEmail(newEmail);
        }
        if (newUsername != null && !newEmail.isEmpty()) {
            user.setUsername(newUsername);
        }

        return userRepository.save(user);
    }


    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }



    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    /**
     * 메인/홈 화면 조회
     * @return 닉네임, img, 도전과제 List
     */
    public ResponseDto searchHome() {
        try {
            // 1. 사용자 정보 조회
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<User> userInfo = userRepository.findByUsername(userDetails.getUsername());

            if (userInfo.isPresent()) {
                // 2. 도전과제 리스트 조회
//                List<GroupResponseDto.UserChallengesDto.ChallengeDto> challenges = challengeRepository.findAll().stream()
//                        .map(challenge -> GroupResponseDto.UserChallengesDto.ChallengeDto.builder()
//                                .id(challenge.getId())
//                                .title(challenge.getTitle())
//                                .img(challenge.getImg())
//                                .build())
//                        .collect(Collectors.toList());
//
//                return GroupResponseDto.UserChallengesDto.builder()
//                        .nickname(user.get().getNickname())
//                        .img(null) // img 보류 상태
//                        .challengesList(challenges)
//                        .build();
                return null;
            } else return new ResponseDto(404, "사용자 정보를 찾을 수 없습니다.");
        } catch (Exception e) {
            return new ResponseDto(500, "메인/홈 화면 조회 error: " + e.getMessage());
        }
    }

}
