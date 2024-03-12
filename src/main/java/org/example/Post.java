package org.example;

import java.time.LocalDateTime;

public class Post {
    private int num;
    private String title;
    private String description;
    private LocalDateTime date;

    public Post(int num, String title, String description, LocalDateTime date) {
        this.num=num;
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public int getNum() {
        return num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
