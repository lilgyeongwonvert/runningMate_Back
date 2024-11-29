package com.demo.album.dto;

public class LoginResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String email;


    public LoginResponseDto(Long id, String username, String nickname, String email){
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email=email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
