package com.example.foodapp.modal;

public class Notify {
    private  String id;
    private String title;
    private String times;
    private int userId;

    public Notify() {
    }

    public Notify(String id, String title, String times, int userId) {
        this.id = id;
        this.title = title;
        this.times = times;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Notify{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", times='" + times + '\'' +
                ", userId=" + userId +
                '}';
    }
}
