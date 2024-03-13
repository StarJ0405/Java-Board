package org.example;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Post {
    private final int num;
    private String title;
    private String description;
    private LocalDateTime date;
    private int show;
    private List<Comment> comments;

    public Post(int num, String title, String description) {
        this(num, title, description, LocalDateTime.now(), 0);
    }

    public Post(int num, String title, String description, LocalDateTime date) {
        this(num, title, description, date, 0);
    }

    public Post(int num, String title, String description, @Nullable LocalDateTime date, @Nullable Integer show) {
        this.num = num;
        this.title = title;
        this.description = description;
        this.date = Optional.ofNullable(date).orElse(LocalDateTime.now());
        this.show = Optional.ofNullable(show).orElse(0);
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

    public int getShow() {
        return show;
    }

    public void show() {
        this.show++;
        System.out.println("==================");
        System.out.println("번호 : " + this.num);
        System.out.println("제목 : " + this.title);
        System.out.println("내용 : " + this.description);
        System.out.println("작성일 :" + this.date.format(View.getDateTimeFormatter()));
        System.out.println("조회수 : " + this.show);
        System.out.println("==================");
        if (comments.size() > 0) {
            System.out.println("=======댓글=======");
            for (Comment comment : comments) {
                System.out.println("댓글 내용 : " + comment.getContent());
                System.out.println("댓글 작성일 : " + comment.getDate().format(View.getDateTimeFormatter()));
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
