package org.example.DBSystem;

import org.example.Comment;
import org.example.Member;
import org.example.Post;

import java.sql.*;
import java.util.List;

public class mySQLStore extends DBStore {
    private String url = "jdbc:mysql://localhost:3306/java_board";
    private final String userName = "root";
    private final String password = "admin";

    public void connect() throws SQLException {
        Connection connection = DriverManager.getConnection(url, userName, password);
//
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery("select * from users");
//        resultSet.next();
//        String name = resultSet.getString("name");
//        System.out.println(name);
//        resultSet.close();
//        statement.close();
//        connection.close();
    }

    @Override
    public void setPost(String key, Post post) {
        try {
            Connection connection = DriverManager.getConnection(url, userName, password);
            Statement statement = connection.createStatement();
            String query = "INSERT INTO post(number, id, title, description, date, _show) values(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE number=?, id=?, title=?, description=?, date=?, _show=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, post.getNum());
            preparedStatement.setString(2, post.getAuthor());
            preparedStatement.setString(3, post.getTitle());
            preparedStatement.setString(4, post.getDescription());
            preparedStatement.setString(5, post.getDate().toString());
            preparedStatement.setInt(6, post.getShow());

            preparedStatement.setInt(7, post.getNum());
            preparedStatement.setString(8, post.getAuthor());
            preparedStatement.setString(9, post.getTitle());
            preparedStatement.setString(10, post.getDescription());
            preparedStatement.setString(11, post.getDate().toString());
            preparedStatement.setInt(12, post.getShow());

            preparedStatement.executeUpdate();

            // comment
            List<Comment> comments = post.getComments();
            for (int i = 0; i < comments.size(); i++) {
                // 해당 게시물 연관 코멘트 전체 삭제
                // 코멘트 기입
            }
            // loves
            // 해당 게시물 연관 좋아요 테이블 전체 삭제
            // 좋아요 기입

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
            String query = "INSERT INTO member(id, password, nickname) values(?,?,?)  ON DUPLICATE KEY UPDATE id=?, password=?, nickname=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, member.getID());
            preparedStatement.setString(2, member.getPassword());
            preparedStatement.setString(3, member.getNickname());

            preparedStatement.setString(4, member.getID());
            preparedStatement.setString(5, member.getPassword());
            preparedStatement.setString(6, member.getNickname());
            System.out.println(preparedStatement.toString());
            preparedStatement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadAllData() {

    }
}
