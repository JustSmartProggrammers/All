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

    public void createBookmark(Bookmark bookmark) throws SQLException {
        bookmarkDAO.createBookmark(bookmark);
    }

    public void deleteBookmark(Long bookmarkId) throws SQLException {
        bookmarkDAO.deleteBookmark(bookmarkId);
    }
}