package com.example.service;

import com.example.dao.BookmarkDAO;
import com.example.model.Bookmark;
import java.sql.SQLException;
import java.util.List;

public class BookmarkService {
    private BookmarkDAO bookmarkDAO;

    public BookmarkService() {
        this.bookmarkDAO = new BookmarkDAO();
    }

    public List<Bookmark> getBookmarksByUserId(Long userId) throws SQLException {
        return bookmarkDAO.getBookmarksByUserId(userId);
    }

    public boolean createBookmark(Bookmark bookmark) throws SQLException {
        if (bookmarkDAO.bookmarkExists(bookmark.getUserId(), bookmark.getSpotId())) {
            return false; // 이미 존재하는 북마크
        }
        return bookmarkDAO.createBookmark(bookmark);
    }


    public void deleteBookmark(Long bookmarkId) throws SQLException {
        bookmarkDAO.deleteBookmark(bookmarkId);
    }
}