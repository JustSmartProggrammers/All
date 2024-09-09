package com.example.controller;

import com.example.model.Comments;
import com.example.service.CommentsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/v1/posts/*/comments/*")
public class CommentsController extends HttpServlet {

    private CommentsService commentsService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        commentsService = new CommentsService();
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        String pathInfo = req.getPathInfo();

        try {
            switch (method) {
                case "GET":
                    handleGet(pathInfo, req, resp);
                    break;
                case "POST":
                    handlePost(pathInfo, req, resp);
                    break;
                case "PUT":
                    handlePut(pathInfo, req, resp);
                    break;
                case "DELETE":
                    handleDelete(pathInfo, req, resp);
                    break;
                default:
                    sendErrorResponse(resp, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed");
            }
        } catch (Exception e) {
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void handleGet(String pathInfo, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        // pathInfo가 null이거나 빈 문자열인 경우에 대한 처리
        if (pathInfo == null || pathInfo.trim().isEmpty()) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Path info is empty or null");
            return;
        }

        // pathInfo 디버깅용 출력
        System.out.println("Received pathInfo: " + pathInfo);

        // 경로에서 여러 개의 슬래시 제거 및 빈 문자열 제거
        String[] pathParts = pathInfo.split("/");
        pathParts = java.util.Arrays.stream(pathParts).filter(part -> !part.isEmpty()).toArray(String[]::new);

        try {
            // Post ID 확인
            if (pathParts.length >= 2 && pathParts[0].matches("\\d+")) {
                Long postId = Long.parseLong(pathParts[0]);

                // 댓글 리스트 조회
                if (pathParts.length == 2 && "comments".equals(pathParts[1])) {
                    List<Comments> commentsList = commentsService.getCommentsByPostId(postId);
                    sendJsonResponse(resp, HttpServletResponse.SC_OK, objectMapper.valueToTree(commentsList));
                }
                // 대댓글 리스트 조회
                else if (pathParts.length == 3 && "comments".equals(pathParts[1]) && pathParts[2].matches("\\d+")) {
                    Long commentId = Long.parseLong(pathParts[2]);
                    List<Comments> repliesList = commentsService.getRepliesByCommentId(commentId);
                    sendJsonResponse(resp, HttpServletResponse.SC_OK, objectMapper.valueToTree(repliesList));
                } else {
                    sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid GET request path");
                }
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid Post ID format");
            }
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid number format: " + e.getMessage());
        }
    }




    private void handlePost(String pathInfo, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String[] pathParts = pathInfo.split("/");
        JsonNode requestBody = readJsonRequest(req);
        Long userId = requestBody.get("userId").asLong();
        String content = requestBody.get("content").asText();

        if (pathParts.length == 4) {
            // 댓글 생성
            Long postId = Long.parseLong(pathParts[2]);
            Comments comment = commentsService.createComment(postId, null, userId, content);
            sendJsonResponse(resp, HttpServletResponse.SC_CREATED, objectMapper.valueToTree(comment));
        } else if (pathParts.length == 5) {
            // 대댓글 생성
            Long postId = Long.parseLong(pathParts[2]);
            Long parentId = Long.parseLong(pathParts[4]);
            Comments reply = commentsService.createComment(postId, parentId, userId, content);
            sendJsonResponse(resp, HttpServletResponse.SC_CREATED, objectMapper.valueToTree(reply));
        } else {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid POST request path");
        }
    }

    private void handlePut(String pathInfo, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 4 || pathParts.length == 5) {
            // 댓글 또는 대댓글 수정
            Long commentId = Long.parseLong(pathParts[pathParts.length - 1]);
            JsonNode requestBody = readJsonRequest(req);
            String content = requestBody.get("content").asText();
            boolean isUpdated = commentsService.updateComment(commentId, content);
            if (isUpdated) {
                sendJsonResponse(resp, HttpServletResponse.SC_OK, createJsonResponse("Comment updated successfully"));
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Comment not found");
            }
        } else {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid PUT request path");
        }
    }

    private void handleDelete(String pathInfo, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String[] pathParts = pathInfo.split("/");
        if (pathParts.length == 4 || pathParts.length == 5) {
            // 댓글 또는 대댓글 삭제
            Long commentId = Long.parseLong(pathParts[pathParts.length - 1]);
            boolean isDeleted = commentsService.deleteComment(commentId);
            if (isDeleted) {
                sendJsonResponse(resp, HttpServletResponse.SC_OK, createJsonResponse("Comment deleted successfully"));
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Comment not found");
            }
        } else {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid DELETE request path");
        }
    }

    private JsonNode readJsonRequest(HttpServletRequest req) throws IOException {
        return objectMapper.readTree(req.getReader());
    }

    private void sendJsonResponse(HttpServletResponse resp, int statusCode, JsonNode jsonResponse) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(jsonResponse.toString());
    }

    private void sendErrorResponse(HttpServletResponse resp, int statusCode, String message) throws IOException {
        ObjectNode errorResponse = objectMapper.createObjectNode();
        errorResponse.put("error", message);
        sendJsonResponse(resp, statusCode, errorResponse);
    }

    private JsonNode createJsonResponse(String message) {
        ObjectNode jsonResponse = objectMapper.createObjectNode();
        jsonResponse.put("message", message);
        return jsonResponse;
    }
}



