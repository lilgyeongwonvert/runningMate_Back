package com.demo.album.controller;

import com.demo.album.dto.ApiResponseDto.ResponseDto;
import com.demo.album.dto.GroupRequestDto;
import com.demo.album.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
public class GroupController {
    private final GroupService groupService;

    /**
     * 그룹 생성
     * @param createDto
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody GroupRequestDto.CreateDto createDto) {
        ResponseDto response = groupService.create(createDto.getGroupName());
        return ResponseEntity.ok(response);
    }

    /**
     * 크루 구성원 추가
     * @param addDto
     */
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody GroupRequestDto.AddDto addDto) {
        ResponseDto response = groupService.add(addDto.getUserId());
        return ResponseEntity.ok(response);
    }

    /**
     * 크루 구성원 조회
     * @param userName
     * @return  그룹에 등록 가능한 사용자 여부
     */

    @GetMapping("/search/{username}")
    public ResponseEntity<?> search(@PathVariable("username") String username) {
        ResponseDto response = groupService.search(username);
        return ResponseEntity.ok(response);
    }

    /**
     * 크루 구성원 삭제
     * @param userId
     * @return 삭제 완료 msg
     */
    @PatchMapping("/remove/{userId}")
    public ResponseEntity<?> remove(@PathVariable("userId") Long userId) {
        ResponseDto response = groupService.remove(userId);
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/create")
//    @Operation(summary = "그룹생성", description = "새로운 그룹을 등록합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "생성 성공"),
//            @ApiResponse(responseCode = "400", description = "잘못된 요청")
//    })
//    @ResponseBody
//    public GroupGanzi createGroup(
//            @Parameter(description = "등록할 프로젝트 정보", required = true) @RequestBody GroupGanzi groupGanzi) {
//
//        GroupGanzi g = groupService.createGroup(groupGanzi.getGroupName());
//        return g;
//    }
}
