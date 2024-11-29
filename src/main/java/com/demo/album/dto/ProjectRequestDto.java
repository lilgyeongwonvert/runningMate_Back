package com.demo.album.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProjectRequestDto {
    private String projectName;
    private String location;
    private MultipartFile url;
    private String content;
    private String updated_at;



    public ProjectRequestDto(String projectName,String location, MultipartFile url, String content,String updated_at){
        this.projectName=projectName;
        this.location=location;
        this.url=url;
        this.content=content;
        this.updated_at=updated_at;
    }
}
