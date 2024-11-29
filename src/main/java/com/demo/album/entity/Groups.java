package com.demo.album.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Table(name = "group_table")
@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Groups {
    @Id
    @Comment(value = "구분자")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 구분자


    @Column(columnDefinition = "varchar(50) not null comment '그룹명'")
    private String groupName;      //그룹명
}
