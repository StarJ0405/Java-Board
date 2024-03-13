package org.example;

import java.util.*;

public class DataStore {
    private static final LinkedHashMap<Integer, Post> posts = new LinkedHashMap<>();
    private static final HashMap<String, Member> members = new HashMap<>();

    private static Member who = null;

    public static boolean hasPost(int num) {
        return posts.containsKey(num);
    }

    public static void addPost(int num, Post post) {
        posts.put(num, post);
    }

    public static void removePost(int num) {
        posts.remove(num);
    }

    public static Set<Integer> getPostList() {
        return posts.keySet();
    }

    public static Collection<Post> getPosts() {
        return posts.values();
    }

    public static int getEmptyNumber() {
        if (!posts.isEmpty()) {
            int max = posts.keySet().stream().max(Comparator.comparingInt(Integer::intValue)).get();
            if (max != posts.size())
                for (int i = 1; i < max; i++)
                    if (!posts.containsKey(i))
                        return i;
            return max + 1;
        } else return 1;
    }

    public static Post getPost(int num) {
        if (posts.containsKey(num))
            return posts.get(num);
        return null;
    }


    public static boolean hasMember(String id) {
        return members.containsKey(id);
    }

    public static void addMember(String id, Member member) {
        members.put(id, member);
    }

    public static Member getMember(String id) {
        if (members.containsKey(id))
            return members.get(id);
        return null;
    }

    public static Member getWho() {
        return who;
    }

    public static void setWho(Member who) {
        DataStore.who = who;
    }
}
