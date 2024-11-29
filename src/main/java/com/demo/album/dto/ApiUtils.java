package com.demo.album.dto;

public class ApiUtils<T> {

    private boolean success;
    private T data;

    // Constructor
    public ApiUtils(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    // Getter and Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
