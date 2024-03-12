package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private int num;
    private String title;
    private String description;
    private LocalDateTime date;
    private int show;
    private List<Comment> comments;

    public Post(int num, String title, String description) {
        this.num = num;
        this.title = title;
        this.description = description;
        this.date = LocalDateTime.now();
        this.show = 0;
        this.comments = new ArrayList<Comment>();
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
        System.out.println("작성일 :" + this.date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss")));
        System.out.println("조회수 : " + this.show);
        System.out.println("==================");
        if (comments.size() > 0) {
            System.out.println("=======댓글=======");
            for (Comment comment : comments) {
                System.out.println("댓글 내용 : " + comment.getContent());
                System.out.println("댓글 작성일 : " + comment.getDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm:ss")));
                System.out.println("==================");
            }
        }
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComments(String comment) {
        addComments(new Comment(comment));
    }

    public void addComments(Comment comment) {
        this.comments.add(comment);
    }
}
