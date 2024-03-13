package org.example.DBSystem;

import org.example.Comment;
import org.example.DataStore;
import org.example.Member;
import org.example.Post;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FileStore extends DBStore {
    private final File file = new File("./store.yml");

    @Override
    public void setPost(String key, Post post) {
        try {
            if (!file.exists()) file.createNewFile();
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(file);
            YamlConfiguration postsYaml = yaml.getConfigurationSection("posts");
            if (post == null) postsYaml.set(key, null);
            else {
                YamlConfiguration postYaml = postsYaml.getConfigurationSection(key);
                postYaml.set("number", post.getNum());
                postYaml.set("author", post.getAuthor());
                postYaml.set("title", post.getTitle());
                postYaml.set("description", post.getDescription());
                postYaml.set("date", post.getDate().toString());
                postYaml.set("show", post.getShow());
                YamlConfiguration commentsYaml = postYaml.getConfigurationSection("comments");
                List<Comment> comments = post.getComments();
                for (int i = 0; i < comments.size(); i++) {
                    YamlConfiguration commentYaml = commentsYaml.getConfigurationSection(i + "");
                    Comment comment = comments.get(i);
                    commentYaml.set("author", comment.getAuthor());
                    commentYaml.set("content", comment.getContent());
                    commentYaml.set("date", comment.getDate().toString());
                }
                postYaml.set("loves", Arrays.asList(post.getLoves().toArray(String[]::new)));
            }
            yaml.save(file);
        } catch (IOException e) {
        }
    }

    @Override
    public void setMember(String key, Member member) {
        try {
            if (!file.exists()) file.createNewFile();
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(file);
            YamlConfiguration membersYaml = yaml.getConfigurationSection("members");
            if (member == null) membersYaml.set(key, null);
            else {
                YamlConfiguration memberYaml = membersYaml.getConfigurationSection(key);
                memberYaml.set("id", member.getID());
                memberYaml.set("password", member.getPassword());
                memberYaml.set("nickname", member.getNickname());
                if (member.isAdmin()) memberYaml.set("admin", true);
            }
            yaml.save(file);
        } catch (IOException e) {
        }
    }

    @Override
    public void loadAllData() {
        try {
            if (!file.exists()) file.createNewFile();
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.load(file);
            // 포스터
            YamlConfiguration postsYaml = yaml.getConfigurationSection("posts");
            for (String postKey : postsYaml.keySet())
                if (postsYaml.isConfigurationSection(postKey)) {
                    YamlConfiguration postYaml = postsYaml.getConfigurationSection(postKey);
                    if (postYaml.isInteger("number") && postYaml.isString("author") && postYaml.isString("title") && postYaml.isString("description") && postYaml.isString("date") && postYaml.isInteger("show")) {
                        int number = postYaml.getInteger("number");
                        Post post = new Post(number, postYaml.getString("author"), postYaml.getString("title"), postYaml.getString("description"), LocalDateTime.parse(postYaml.getString("date")), postYaml.getInteger("show"));
                        DataStore.addPost(number, post);
                        if (postYaml.isConfigurationSection("comments")) {
                            YamlConfiguration commentsYaml = postYaml.getConfigurationSection("comments");
                            List<Comment> comments = post.getComments();
                            for (String numKey : commentsYaml.keySet())
                                if (commentsYaml.isConfigurationSection(numKey)) {
                                    YamlConfiguration commentYaml = commentsYaml.getConfigurationSection(numKey);
                                    if (commentYaml.isString("author") && commentYaml.isString("content") && commentYaml.isString("date"))
                                        comments.add(new Comment(commentYaml.getString("author"), commentYaml.getString("content"), LocalDateTime.parse(commentYaml.getString("date"))));
                                }
                        }
                        if (postYaml.isList("loves"))
                            post.getLoves().addAll(postYaml.getStringList("loves"));
                    }
                }
            // 멤버
            YamlConfiguration membersYaml = yaml.getConfigurationSection("members");
            for (String memberKey : membersYaml.keySet())
                if (membersYaml.isConfigurationSection(memberKey)) {
                    YamlConfiguration memberYaml = membersYaml.getConfigurationSection(memberKey);
                    if (memberYaml.isString("id") && memberYaml.isString("password") && memberYaml.isString("nickname")) {
                        String id = memberYaml.getString("id");
                        Member member = new Member(id, memberYaml.getString("password"), memberYaml.getString("nickname"));
                        if (memberYaml.isBoolean("admin")) member.setAdmin(memberYaml.getBoolean("admin"));
                        DataStore.addMember(id, member);
                    }
                }
        } catch (IOException e) {
        }
    }
}
