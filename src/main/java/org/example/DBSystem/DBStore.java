package org.example.DBSystem;

import org.example.DataStore;
import org.example.Member;
import org.example.Post;

import java.io.File;
import java.io.IOException;

public abstract class DBStore {
    public static final FileStore FILE_STORE = new FileStore();
    public static final mySQLStore MY_SQL_STORE = new mySQLStore();
    protected final String saveType;

    public DBStore(String saveType) {
        this.saveType = saveType;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setPost(Post post) {
        setPost(post.getNum() + "", post);
    }

    public abstract void setPost(String key, Post post);

    public void setMember(Member member) {
        setMember(member.getID(), member);
    }

    public abstract void setMember(String key, Member member);

    public abstract void loadAllData();

    public void transferedFromOthers() {
        for (Member member : DataStore.getMembers())
            setMember(member);
        for (Post post : DataStore.getPosts())
            setPost(post);
        try {
            if (!defaultConfig.exists())
                defaultConfig.createNewFile();
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(defaultConfig);
            yaml.set("saveType", saveType);
            yaml.save(defaultConfig);
        } catch (IOException e) {
        }
    }

    public static final File defaultConfig = new File("./config.yml");

    public static DBStore checkDBStore() {
        try {
            if (!defaultConfig.exists())
                defaultConfig.createNewFile();
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(defaultConfig);
            if (!yaml.has("saveType")) {
                yaml.set("saveType", FILE_STORE.saveType);
                yaml.save(defaultConfig);
            } else {
                String saveType = yaml.getString("saveType");
                if (saveType.equals(MY_SQL_STORE.saveType))
                    return new mySQLStore();
            }
        } catch (IOException e) {
        }
        return new FileStore();
    }
}
