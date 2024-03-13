package org.example;

import org.example.FileSystem.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class FileStore {
    private static final File file = new File("./store.yml");

    public static void setPost(String key, Post post) {
        try {
            if (!file.exists())
                file.createNewFile();
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(file);
            YamlConfiguration postsYaml = yaml.getConfigurationSection("posts");
            YamlConfiguration postYaml = postsYaml.getConfigurationSection(post.getNum() + "");
            postYaml.set("number", post.getNum());
            postYaml.set("title", post.getTitle());
            postYaml.set("description", post.getDescription());
            postYaml.set("date", post.getDate().toString());
            postYaml.set("show", post.getShow());
            yaml.save(file);
        } catch (IOException e) {
        }
    }

    public static void setMember(String key, Member member) {
        try {
            if (!file.exists())
                file.createNewFile();
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(file);
            YamlConfiguration membersYaml = yaml.getConfigurationSection("members");
            YamlConfiguration memberYaml = membersYaml.getConfigurationSection(member.getID());
            memberYaml.set("id", member.getID());
            memberYaml.set("password", member.getPassword());
            memberYaml.set("nickname", member.getNickname());
            yaml.save(file);
        } catch (IOException e) {
        }
    }

    public static void loadAllData() {
        try {
            if (!file.exists())
                file.createNewFile();
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(file);
            // 포스터
            YamlConfiguration postsYaml = yaml.getConfigurationSection("posts");
            for (String postKey : postsYaml.keySet())
                if (postsYaml.isConfigurationSection(postKey)) {
                    YamlConfiguration postYaml = postsYaml.getConfigurationSection(postKey);
                    if (postYaml.isInteger("number") && postYaml.isString("title") && postYaml.isString("description") && postYaml.isString("date") && postYaml.isInteger("show")) {
                        int number = postYaml.getInteger("number");
                        DataStore.addPost(number, new Post(number, postYaml.getString("title"), postYaml.getString("description"), LocalDateTime.parse(postYaml.getString("date")), postYaml.getInteger("show")));
                    }
                }
            // 멤버
            YamlConfiguration membersYaml = yaml.getConfigurationSection("members");
            for (String memberKey : membersYaml.keySet())
                if (membersYaml.isConfigurationSection(memberKey)) {
                    YamlConfiguration memberYaml = membersYaml.getConfigurationSection(memberKey);
                    if (memberYaml.isString("id") && memberYaml.isString("password") && memberYaml.isString("nickname")) {
                        String id = memberYaml.getString("id");
                        DataStore.addMember(id, new Member(id, memberYaml.getString("password"), memberYaml.getString("nickname")));
                    }
                }
        } catch (IOException e) {
        }
    }
}
