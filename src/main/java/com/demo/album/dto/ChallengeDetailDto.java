package com.demo.album.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeDetailDto {
    private Long id;
    private String period;
    private String location;
    private String fee;
    private String description;
    private String imageUrl;
}
