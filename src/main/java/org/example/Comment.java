package org.example;

import java.time.LocalDateTime;

public class Comment {
    private String content;
    private LocalDateTime date;

    public Comment(String content) {
        this.content = content;
        this.date = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
