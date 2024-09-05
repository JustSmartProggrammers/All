package com.example.controller;

import com.example.model.Bookmark;
import com.example.service.BookmarkService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/v1/bookmark/*")
public class BookmarkController extends HttpServlet {
    private BookmarkService bookmarkService;
    private Gson gson;

    public void init() {
        bookmarkService = new BookmarkService();
        gson = new Gson();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.startsWith("/user/")) {
            Long userId = Long.parseLong(pathInfo.split("/")[2]);
            try {
                List<Bookmark> bookmarks = bookmarkService.getBookmarksByUserId(userId);
                sendJsonResponse(response, gson.toJson(bookmarks));
            } catch (SQLException e) {
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "데이터베이스 오류 발생");
            }
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = request.getReader().readLine()) != null) {
            sb.append(line);
        }
        Bookmark bookmark = gson.fromJson(sb.toString(), Bookmark.class);

        try {
            bookmarkService.createBookmark(bookmark);
            sendJsonResponse(response, gson.toJson("북마크가 생성되었습니다."));
        } catch (SQLException e) {
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "데이터베이스 오류 발생");
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            Long bookmarkId = Long.parseLong(pathInfo.substring(1));
            try {
                bookmarkService.deleteBookmark(bookmarkId);
                sendJsonResponse(response, gson.toJson("북마크가 삭제되었습니다."));
            } catch (SQLException e) {
                sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "데이터베이스 오류 발생");
            }
        } else {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청");
        }
    }

    private void sendJsonResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        sendJsonResponse(response, gson.toJson(message));
    }
}