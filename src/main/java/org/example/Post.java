package org.example;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Post {
    private String title;
    private String description;

    private LocalDateTime date;

    public Post(String title, String description, LocalDateTime date) {
        this.title = title;
        this.description = description;
        this.date = date;
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
