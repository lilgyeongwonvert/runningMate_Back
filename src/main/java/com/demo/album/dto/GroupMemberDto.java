package com.demo.album.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupMemberDto {
    private Long userId;
    private String nickname;
    private String username;

    public GroupMemberDto(String username, Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
        this.username = username;
    }
}
