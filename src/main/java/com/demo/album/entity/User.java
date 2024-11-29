package com.demo.album.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Builder(toBuilder = true)
@Getter
@Setter
@Entity
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean firstLogin = true; // 기본값은 true로 설정

    @Column(nullable=false)
    private int point=0;

    @ManyToOne
    @Comment(value = "그룹 ID")
    @JoinColumn(name = "group_id", nullable = true)
    private Groups group;


    // 기본 생성자
    public User() {}


    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    // 모든 필드를 포함한 생성자
    public User(String username, String password, String nickname, String email) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }


    public User(Long userId, String username, String nickname, String email) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }


}
