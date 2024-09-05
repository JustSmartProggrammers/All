package com.example.dao;

import com.example.util.DBUtil;
import com.example.model.Bookmark;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookmarkDAO {

    public List<Bookmark> getBookmarksByUserId(Long userId) throws SQLException {
        List<Bookmark> bookmarks = new ArrayList<>();
        String sql = "SELECT * FROM bookmark WHERE userId = ? AND isDeleted = false";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookmarks.add(extractBookmarkFromResultSet(rs));
                }
            }
        }

        return bookmarks;
    }

    public boolean createBookmark(Bookmark bookmark) throws SQLException {
        String sql = "INSERT INTO bookmark (userId, spotId, isDeleted) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, bookmark.getUserId());
            pstmt.setLong(2, bookmark.getSpotId());
            pstmt.setBoolean(3, false);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean deleteBookmark(Long bookmarkId) throws SQLException {
        String sql = "UPDATE bookmark SET isDeleted = true WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, bookmarkId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public Bookmark getBookmarkById(Long bookmarkId) throws SQLException {
        String sql = "SELECT * FROM bookmark WHERE id = ? AND isDeleted = false";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, bookmarkId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractBookmarkFromResultSet(rs);
                }
            }
        }
        return null;
    }

    private Bookmark extractBookmarkFromResultSet(ResultSet rs) throws SQLException {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(rs.getLong("id"));
        bookmark.setUserId(rs.getLong("userId"));
        bookmark.setSpotId(rs.getLong("spotId"));
        bookmark.setDeleted(rs.getBoolean("isDeleted"));
        return bookmark;
    }
}