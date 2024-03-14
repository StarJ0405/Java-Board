package org.example.DBSystem;

public final class SQLQuery {
    public final static String POST_SELECT = "SELECT * FROM post";
    public final static String POST_INSERT = "INSERT INTO post(number, id, title, description, date, _show) values(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE number=?, id=?, title=?, description=?, date=?, _show=?";
    public final static String POST_DELETE_ALL = "DELETE FROM post";
    public final static String COMMENT_SELECT = "SELECT * FROM comment WHERE number=?";
    public final static String COMMENT_INSERT = "INSERT INTO comment(id,date,number,content) values (?,?,?,?) ON DUPLICATE KEY UPDATE id=?, date=?, number=?,content=?";
    public final static String COMMENT_DELETE = "DELETE FROM comment where number= ?";
    public final static String COMMENT_DELETE_ALL = "DELETE FROM comment";
    public final static  String LOVE_SELECT = "SELECT * FROM love WHERE number=?";
    public final static String LOVE_INSERT = "INSERT INTO love(number,id) values(?,?) ON DUPLICATE KEY UPDATE number=?,id=?";
    public final static String LOVE_DELETE = "DELETE FROM love WHERE number= ?";
    public final static String LOVE_DELETE_ALL = "DELETE FROM love";
    public final static String MEMBER_SELECT = "SELECT * FROM member";
    public final static String MEMBER_INSERT = "INSERT INTO member(id, password, nickname,admin) values(?,?,?,?)  ON DUPLICATE KEY UPDATE id=?, password=?, nickname=?, admin=?";
    public final static String MEMBER_DELETE_ALL = "DELETE FROM member";
}
