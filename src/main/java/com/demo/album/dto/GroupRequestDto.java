package com.demo.album.dto;

import lombok.*;

public class GroupRequestDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class CreateDto {
        private String groupName;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class AddDto {
        private String userId;
    }
}
