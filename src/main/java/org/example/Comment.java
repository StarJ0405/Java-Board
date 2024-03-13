package org.example;

import java.time.LocalDateTime;

public class Comment {
    private final String author;
    private String content;
    private LocalDateTime date;

    public Comment(String author, String content) {
        this(author, content, LocalDateTime.now());
    }

    public Comment(String author, String content, LocalDateTime date) {
        this.author = author;
        this.content = content;
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
