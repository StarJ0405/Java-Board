package org.example.DBSystem;

import org.example.Member;
import org.example.Post;

public abstract class DBStore {
    public abstract void setPost(String key, Post post);

    public abstract void setMember(String key, Member member);

    public abstract void loadAllData();
}
