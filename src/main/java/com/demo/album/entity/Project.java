package com.demo.album.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false, unique = true)
    private Long projectId;

    @Column(name = "project_name", nullable = false, unique = true)
    private String projectName;

    @Column(nullable = false, unique = true)
    private String location;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(nullable = false, unique = true)
    private String content;

    @Column(nullable = false, unique = true)
    private String updated_at;

    @ManyToOne
    @Comment(value = "그룹 ID")
    @JoinColumn(name = "group_id", nullable = true)
    private Groups group;

    public Project(String projectName,String location,String url,String content,String updated_at,Groups group){
        this.projectName = projectName;
        this.location = location;
        this.url = url;
        this.content = content;
        this.updated_at=updated_at;
        this.group = group;
    }

    public Project(String projectName,String location,String url,String content,String updated_at){
        this.projectName = projectName;
        this.location = location;
        this.url = url;
        this.content = content;
        this.updated_at=updated_at;
    }



    public Project() {

    }
}
