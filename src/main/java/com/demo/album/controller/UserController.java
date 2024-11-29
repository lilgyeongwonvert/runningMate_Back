package com.demo.album.controller;

import com.demo.album.dto.ApiResponseDto.ResponseDto;
import com.demo.album.dto.LoginRequestDto;
import com.demo.album.dto.LoginResponseDto;
import com.demo.album.dto.RegisterRequestDto;
import com.demo.album.entity.User;
import com.demo.album.repository.UserRepository;
import com.demo.album.service.UserService;
import com.demo.album.util.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;
    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider,UserRepository userRepository) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository=userRepository;
    }

    @PostMapping("/register")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 사용자 이름 중복")
    })
    @ResponseBody
    public ResponseDto registerUser(
            @Parameter(description = "등록할 사용자 정보", required = true) @RequestBody RegisterRequestDto user) {

        ResponseDto response = userService.registerUser(
                    user.getUsername(), user.getPassword(), user.getNickname(), user.getEmail()
            );
            //var Dto = new LoginResponseDto(registeredUser.getUserId(),registeredUser.getUsername(),registeredUser.getNickname(),registeredUser.getEmail());
            return response;

    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자가 아이디와 비밀번호로 로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 잘못되었습니다.")
    })
    public ResponseEntity<Map<String, Object>> loginUser(
            @Parameter(description = "로그인할 사용자 정보", required = true) @RequestBody LoginRequestDto loginRequestDto) {
        Optional<User> authenticatedUser = userService.authenticateUser(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        if (authenticatedUser.isPresent()) {
            // 토큰 생성
            String token = jwtTokenProvider.createToken(loginRequestDto.getUsername());

            // 사용자 정보 조회
            Optional<User> userInfo = userService.findByUsername(loginRequestDto.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("msg", "로그인되었습니다.");
            response.put("token", token);
            response.put("userId", userInfo.get().getUserId());
            response.put("email", userInfo.get().getEmail());
            response.put("nickname", userInfo.get().getNickname());
            response.put("username", userInfo.get().getUsername());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "아이디 또는 비밀번호가 잘못되었습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }


    @PutMapping("/updateUser")
    @Operation(summary = "회원정보수정", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 사용자 이름 중복")
    })
    public ResponseEntity<Map<String, Object>> updateUser(
            @Parameter(description = "수정할 사용자 정보", required = true) @RequestBody RegisterRequestDto updateUser) {

        try {
            // 현재 인증된 사용자의 사용자 이름을 가져옵니다.
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();

            // 사용자 정보 업데이트
            User updatedUser = userService.updateUser(
                    username,
                    updateUser.getPassword(),
                    updateUser.getNickname(),
                    updateUser.getEmail(),
                    updateUser.getUsername()
            );

            // 업데이트된 사용자 정보로 응답 작성
            Map<String, Object> response = new HashMap<>();
            response.put("id", updatedUser.getUserId());
            response.put("username", updatedUser.getUsername());
            response.put("nickname", updatedUser.getNickname());
            response.put("email", updatedUser.getEmail());

            return ResponseEntity.ok(response); // 200 OK로 응답
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/check-username")
    @Operation(summary = "아이디 중복 확인", description = "아이디의 사용 가능 여부를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 사용 가능"),
            @ApiResponse(responseCode = "409", description = "아이디 사용 불가능")
    })
    public ResponseEntity<Map<String, Object>> checkUsername(
            @Parameter(description = "확인할 아이디", required = true) @RequestParam String username) {

        boolean isAvailable = userService.isUsernameAvailable(username);
        Map<String, Object> response = new HashMap<>();

        if (isAvailable) {
            response.put("available", true);
            response.put("message", "사용 가능한 아이디입니다.");
            return ResponseEntity.ok(response); // 200 OK
        } else {
            response.put("available", false);
            response.put("message", "이미 사용 중인 아이디입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // 409 Conflict
        }
    }

    @GetMapping("/home")
    public ResponseEntity<?> home() {
        ResponseDto response = userService.searchHome();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/search/{userId}")
    public ResponseEntity<?> search(@PathVariable("userId") Long userId) {
        ResponseDto response = userService.search(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(
            @Parameter(description = "JWT 토큰", required = true) @RequestHeader("Authorization") String token) {
        String tokenWithoutBearer = token.replace("Bearer ", "");
        jwtTokenProvider.invalidateToken(tokenWithoutBearer);
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    @PostMapping("/check-first-login")
    public ResponseEntity<Map<String, Object>> checkFirstLogin(
            @Parameter(description = "JWT 토큰", required = true) @RequestHeader("Authorization") String token) {

        // JWT에서 사용자 ID 또는 이메일 추출
        String username = jwtTokenProvider.getUsername(token.replace("Bearer ", ""));

        // 사용자 정보 조회
        Optional<User> user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "사용자를 찾을 수 없습니다."));
        }

        // 첫 로그인 여부 확인
        boolean isFirstLogin = user.get().isFirstLogin();

        // 첫 로그인이면 상태 업데이트
        if (isFirstLogin) {
            user.get().setFirstLogin(false); // 첫 로그인 여부를 false로 변경
            userRepository.save(user.get()); // 변경된 상태 저장
        }

        // 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("isFirstLogin", isFirstLogin);
        response.put("message", isFirstLogin ? "첫 로그인입니다." : "이미 로그인한 적이 있습니다.");

        return ResponseEntity.ok(response);
    }







}
