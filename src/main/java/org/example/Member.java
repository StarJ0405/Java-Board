package org.example;

import java.util.Optional;

public class Member {
    private final String id;
    private String password;
    private String nickname;
    private boolean admin;

    public Member(String id, String password, String nickname) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
    }

    public String getID() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = Optional.ofNullable(admin).orElse(false);
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
