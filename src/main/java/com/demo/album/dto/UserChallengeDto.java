package com.demo.album.dto;

public class UserChallengeDto {
    private Long id;
    private String category;
    private String title;
    private int dday;
    private int points;
    private String period;
    private String location;
    private String fee;
    private String description;
    private String uploadDate;

    // 기본 생성자
    public UserChallengeDto() {}

    // 모든 필드를 포함한 생성자
    public UserChallengeDto(Long id, String category, String title, int dday, int points, String period, String location, String fee, String description, String uploadDate) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.dday = dday;
        this.points = points;
        this.period = period;
        this.location = location;
        this.fee = fee;
        this.description = description;
        this.uploadDate = uploadDate;
    }

    // Getter 메소드들
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

    public String getPeriod() {
        return period;
    }

    public String getLocation() {
        return location;
    }

    public String getFee() {
        return fee;
    }

    public String getDescription() {
        return description;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    // Setter 메소드들
    public void setId(Long id) {
        this.id = id;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDday(int dday) {
        this.dday = dday;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}
