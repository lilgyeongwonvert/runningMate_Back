package com.demo.album.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseDto {
    private Long userId;
    private String nickname;
    private String groupName;
    private int point;
    private List<ProjectDto> projects;
    private List<GroupMemberDto> members;

    public UserResponseDto(int point, Long userId, String nickname, String groupName, List<ProjectDto> projects, List<GroupMemberDto> members) {
        this.userId = userId;
        this.nickname = nickname;
        this.groupName = groupName;
        this.projects = projects;
        this.members = members;
        this.point = point;

    }
}
