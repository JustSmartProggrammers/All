 package com.example.dao;

import com.example.model.Posts;
import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostsDAO {

    public List<Posts> getAllPosts() throws SQLException {
        List<Posts> postsList = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE isDeleted = FALSE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Posts post = new Posts();
                post.setId(rs.getLong("id"));
                post.setUserId(rs.getLong("userId"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                post.setDeleted(rs.getBoolean("isDeleted"));
                postsList.add(post);
            }
        }
        return postsList;
    }

    public List<Posts> getPostsByUserId(Long userId) throws SQLException {
        List<Posts> postsList = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE userId = ? AND isDeleted = FALSE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Posts post = new Posts();
                    post.setId(rs.getLong("id"));
                    post.setUserId(rs.getLong("userId"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    post.setDeleted(rs.getBoolean("isDeleted"));
                    postsList.add(post);
                }
            }
        }
        return postsList;
    }

    public Optional<Posts> getPostById(Long id) throws SQLException {
        String sql = "SELECT * FROM posts WHERE id = ? AND isDeleted = FALSE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Posts post = new Posts();
                    post.setId(rs.getLong("id"));
                    post.setUserId(rs.getLong("userId"));
                    post.setTitle(rs.getString("title"));
                    post.setContent(rs.getString("content"));
                    post.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    post.setDeleted(rs.getBoolean("isDeleted"));
                    return Optional.of(post);
                }
            }
        }
        return Optional.empty();
    }

    public Posts createPost(Posts post) throws SQLException {
        String sql = "INSERT INTO posts (userId, title, content, createdAt, isDeleted) VALUES (?, ?, ?, ?, FALSE)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, post.getUserId());
            stmt.setString(2, post.getTitle());
            stmt.setString(3, post.getContent());
            stmt.setTimestamp(4, Timestamp.valueOf(post.getCreatedAt()));

            stmt.executeUpdate();

            // 생성된 ID를 가져오는 부분 추가
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getLong(1));
                }
            }
        }
        return post;
    }

    public boolean updatePost(Long id, String title, String content) throws SQLException {
        String sql = "UPDATE posts SET title = ?, content = ? WHERE id = ? AND isDeleted = FALSE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setLong(3, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deletePost(Long id) throws SQLException {
        String sql = "UPDATE posts SET isDeleted = TRUE WHERE id = ? AND isDeleted = FALSE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
