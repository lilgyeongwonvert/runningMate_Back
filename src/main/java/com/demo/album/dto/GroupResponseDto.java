package com.demo.album.dto;

import lombok.*;

import java.util.List;

public class GroupResponseDto {
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
    public static class UserInfoDto {
        private Long userId;
        private String username;
        private String nickname;
        private String email;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class UserChallengesDto {
        private String nickname;
        private String img;

        private List<ChallengeDto> challengesList;

        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor
        @Builder
        public static class ChallengeDto {
            private Long id;
            private String title;
            private String img;
        }
    }
}
