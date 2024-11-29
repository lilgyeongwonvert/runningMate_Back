package com.demo.album.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDto {
    private Long projectId;
    private String projectName;
    private String url;
    private String updatedAt;

    public ProjectDto(Long projectId, String projectName, String url, String updatedAt) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.url = url;
        this.updatedAt = updatedAt;
    }
}
