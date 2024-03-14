package org.example.DBSystem;

import org.example.Comment;
import org.example.DataStore;
import org.example.Member;
import org.example.Post;

import java.sql.*;
import java.time.LocalDateTime;

public class mySQLStore extends DBStore {
    private final String url = "jdbc:mysql://localhost:3306/java_board";
    private final String userName = "root";
    private final String password = "admin";

    protected mySQLStore() {
        super("mySQL");
    }


    private void saveQuery(Connection connection, Statement statement, String query, WrapValue... wrapValues) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (WrapValue wrapValue : wrapValues)
            wrapValue.apply(preparedStatement);
        preparedStatement.executeUpdate();
    }

    @Override
    public void setPost(String key, Post post) {
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();
            // 게시글
            saveQuery(connection, statement, SQLQuery.POST_INSERT, WrapValue.getInt(post.getNum(), 1, 7), WrapValue.getString(post.getAuthor(), 2, 8), WrapValue.getString(post.getTitle(), 3, 9), WrapValue.getString(post.getDescription(), 4, 10), WrapValue.getString(post.getDate().toString(), 5, 11), WrapValue.getInt(post.getShow(), 6, 12));
            // 댓글
            saveQuery(connection, statement, SQLQuery.COMMENT_DELETE, WrapValue.getInt(post.getNum(), 1));
            for (Comment comment : post.getComments())
                saveQuery(connection, statement, SQLQuery.COMMENT_INSERT, WrapValue.getString(comment.getAuthor(), 1, 5), WrapValue.getString(comment.getDate().toString(), 2, 6), WrapValue.getInt(post.getNum(), 3, 7), WrapValue.getString(comment.getContent(), 4, 8));
            // 좋아요
            saveQuery(connection, statement, SQLQuery.LOVE_DELETE, WrapValue.getInt(post.getNum(), 1));
            for (String love : post.getLoves())
                saveQuery(connection, statement, SQLQuery.LOVE_INSERT, WrapValue.getInt(post.getNum(), 1, 3), WrapValue.getString(post.getAuthor(), 2, 4));
            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setMember(String key, Member member) {
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();
            saveQuery(connection, statement, SQLQuery.MEMBER_INSERT, WrapValue.getString(member.getID(), 1, 5), WrapValue.getString(member.getPassword(), 2, 6), WrapValue.getString(member.getNickname(), 3, 7), WrapValue.getInt(member.isAdmin(), 4, 8));
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ResultSet loadQuery(Connection connection, Statement statement, String query, WrapValue... wrapValues) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (WrapValue wrapValue : wrapValues)
            wrapValue.apply(preparedStatement);
        return preparedStatement.executeQuery();
    }

    @Override
    public void loadAllData() {
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();
            // 게시글
            ResultSet postResult = loadQuery(connection, statement, SQLQuery.POST_SELECT);
            while (postResult.next()) {
                int number = postResult.getInt("number");
                String author = postResult.getString("id");
                String title = postResult.getString("title");
                String description = postResult.getString("description");
                LocalDateTime date = LocalDateTime.parse(postResult.getString("date"));
                int show = postResult.getInt("_show");
                Post post = new Post(number, author, title, description, date, show);
                DataStore.addPost(number, post);
                // 댓글
                ResultSet commentResult = loadQuery(connection, statement, SQLQuery.COMMENT_SELECT, WrapValue.getInt(number, 1));
                while (commentResult.next()) {
                    String commentAuthor = commentResult.getString("id");
                    String commentContent = commentResult.getString("content");
                    LocalDateTime commentDate = LocalDateTime.parse(commentResult.getString("date"));
                    post.addComments(new Comment(commentAuthor, commentContent, commentDate));
                }
                commentResult.close();
                // 좋아요
                ResultSet loveResult = loadQuery(connection, statement, SQLQuery.LOVE_SELECT, WrapValue.getInt(number, 1));
                while (loveResult.next()) post.addLoves(loveResult.getString("id"));
                loveResult.close();
            }
            postResult.close();
            // 멤버
            ResultSet memberResult = loadQuery(connection, statement, SQLQuery.MEMBER_SELECT);
            while (memberResult.next()) {
                String id = memberResult.getString("id");
                String password = memberResult.getString("password");
                String nickname = memberResult.getString("nickname");
                Boolean admin = memberResult.getBoolean("admin");
                Member member = new Member(id, password, nickname);
                member.setAdmin(admin);
                DataStore.addMember(id, member);
            }
            memberResult.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void transferedFromOthers() {
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();
            saveQuery(connection, statement, SQLQuery.LOVE_DELETE_ALL);
            saveQuery(connection, statement, SQLQuery.COMMENT_DELETE_ALL);
            saveQuery(connection, statement, SQLQuery.POST_DELETE_ALL);
            saveQuery(connection, statement, SQLQuery.MEMBER_DELETE_ALL);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.transferedFromOthers();
    }
}
