package com.demo.album.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

//@Entity
@Getter
@Setter
public class GroupGanzi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", nullable = false, unique = true)
    private Long groupId;

    @Column(nullable = false, unique = true)
    private String groupName;

    public GroupGanzi(String groupName){
        this.groupName = groupName;
    }

    public GroupGanzi() {}
}
