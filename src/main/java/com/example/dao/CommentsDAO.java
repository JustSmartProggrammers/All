package com.example.dao;

import com.example.model.Comments;
import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommentsDAO {

    public List<Comments> getCommentsByPostId(Long postId) throws SQLException {
        List<Comments> commentsList = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE postId = ? AND parentId IS NULL AND isDeleted = FALSE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, postId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    commentsList.add(mapResultSetToComment(rs));
                }
            }
        }
        return commentsList;
    }

    public List<Comments> getRepliesByCommentId(Long commentId) throws SQLException {
        List<Comments> repliesList = new ArrayList<>();
        String sql = "SELECT * FROM comments WHERE parentId = ? AND isDeleted = FALSE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, commentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    repliesList.add(mapResultSetToComment(rs));
                }
            }
        }
        return repliesList;
    }

    public Optional<Comments> getCommentById(Long commentId) throws SQLException {
        String sql = "SELECT * FROM comments WHERE id = ? AND isDeleted = FALSE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, commentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToComment(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Comments createComment(Comments comment) throws SQLException {
        String sql = "INSERT INTO comments (postId, parentId, userId, content, createdAt, isDeleted) VALUES (?, ?, ?, ?, ?, FALSE)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, comment.getPostId());
            stmt.setObject(2, comment.getParentId(), Types.BIGINT);
            stmt.setLong(3, comment.getUserId());
            stmt.setString(4, comment.getContent());
            stmt.setTimestamp(5, Timestamp.valueOf(comment.getCreatedAt()));
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    comment.setId(generatedKeys.getLong(1));
                }
            }
        }
        return comment;
    }

    public boolean updateComment(Long commentId, String content) throws SQLException {
        String sql = "UPDATE comments SET content = ? WHERE id = ? AND isDeleted = FALSE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, content);
            stmt.setLong(2, commentId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteComment(Long commentId) throws SQLException {
        String sql = "UPDATE comments SET isDeleted = TRUE WHERE id = ? AND isDeleted = FALSE";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, commentId);
            return stmt.executeUpdate() > 0;
        }
    }

    private Comments mapResultSetToComment(ResultSet rs) throws SQLException {
        Comments comment = new Comments();
        comment.setId(rs.getLong("id"));
        comment.setPostId(rs.getLong("postId"));
        comment.setParentId((Long) rs.getObject("parentId"));
        comment.setUserId(rs.getLong("userId"));
        comment.setContent(rs.getString("content"));
        comment.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        comment.setDeleted(rs.getBoolean("isDeleted"));
        return comment;
    }
}
