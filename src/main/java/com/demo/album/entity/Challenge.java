package com.demo.album.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String category;
    private String title;
    private int dday;
    private int points;

 // 이거 새로운 엔티티 만들고 거기에 넣어서 컨트롤러에
    // 내가 작성한 도전과제 목록 반환 하는거에 불러오기 그 새로만든 엔티티를 반환받기 그래갸 여러개 만들수았고, 유저 불러올수있음
    // 새로운 필드 추가
    private String period;
    private String location;
    private String fee;
    private String description;


    public Challenge() {}

    public Challenge(String category, String title, int dday, int points, String period, String location, String fee, String description) {
        this.category = category;
        this.title = title;
        this.dday = dday;
        this.points = points;
        this.period = period;
        this.fee = fee;
        this.description = description;
        this.location = location;
    }

    // Getter와 Setter 추가
    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public int getDday() {
        return dday;
    }

    public int getPoints() {
        return points;
    }




    // 새로운 필드들의 Getter와 Setter
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
