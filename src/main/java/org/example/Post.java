package org.example;

import java.time.LocalDateTime;

public class Post {
    private int num;
    private String title;
    private String description;
    private LocalDateTime date;
    private int show;

    public Post(int num, String title, String description, LocalDateTime date) {
        this.num = num;
        this.title = title;
        this.description = description;
        this.date = date;
        this.show = 0;
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

    public void show() {
        this.show++;
        System.out.println("==================");
        System.out.println("번호 : " + this.num);
        System.out.println("제목 : " + this.title);
        System.out.println("내용 : " + this.description);
        System.out.println("조회수 : " + this.show);
        System.out.println("==================");
    }
}
