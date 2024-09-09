package com.example.controller;

import com.example.model.Posts;
import com.example.service.PostsService;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/api/v1/posts/*")
public class PostsController extends HttpServlet {

    private PostsService postsService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        postsService = new PostsService();
        objectMapper = new ObjectMapper();

        // LocalDateTime 직렬화 설정 추가
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.format(DateTimeFormatter.ISO_DATE_TIME));
            }
        });
        objectMapper.registerModule(module);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        String pathInfo = req.getPathInfo();
        System.out.println("Received " + method + " request with pathInfo: " + pathInfo); // 디버깅 로그

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
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid number format: " + e.getMessage());
        } catch (Exception e) {
            log("Unexpected error occurred: ", e);
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    // GET 요청 처리
    private void handleGet(String pathInfo, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        if (pathInfo == null || pathInfo.equals("/")) {
            // 전체 게시글 조회
            List<Posts> postsList = postsService.getAllPosts();
            sendJsonResponse(resp, HttpServletResponse.SC_OK, objectMapper.valueToTree(postsList));
        } else if (pathInfo.startsWith("/user/")) {
            // 특정 유저의 게시글 조회
            String userIdStr = pathInfo.substring("/user/".length());
            Long userId = Long.parseLong(userIdStr);
            List<Posts> postsList = postsService.getPostsByUserId(userId);
            sendJsonResponse(resp, HttpServletResponse.SC_OK, objectMapper.valueToTree(postsList));
        } else {
            // 특정 게시글 조회
            try {
                Long postId = Long.parseLong(pathInfo.substring(1));
                Posts post = postsService.getPostById(postId);
                if (post != null) {
                    sendJsonResponse(resp, HttpServletResponse.SC_OK, objectMapper.valueToTree(post));
                } else {
                    sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Post not found");
                }
            } catch (NumberFormatException e) {
                sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid Post ID format");
            }
        }
    }

    // POST 요청 처리
    private void handlePost(String pathInfo, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        // pathInfo가 null이거나 "/" 또는 ""(빈 문자열)일 때 요청을 처리하도록 수정
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {
            // JSON 요청 바디를 읽고 게시글 생성 처리
            try {
                JsonNode requestBody = readJsonRequest(req);
                Long userId = requestBody.get("userId").asLong();
                String title = requestBody.get("title").asText();
                String content = requestBody.get("content").asText();

                // PostsService를 통해 게시글 생성
                Posts createdPost = postsService.createPost(userId, title, content);
                // 생성된 게시글을 응답으로 반환
                sendJsonResponse(resp, HttpServletResponse.SC_CREATED, objectMapper.valueToTree(createdPost));
            } catch (JsonProcessingException e) {
                // JSON 파싱 오류 처리
                sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format: " + e.getMessage());
            } catch (Exception e) {
                // 그 외의 예외 처리
                sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating post: " + e.getMessage());
            }
        } else {
            // /api/v1/posts 이외의 경로로 POST 요청이 들어왔을 때 처리
            sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Invalid POST request path");
        }
    }


    // PUT 요청 처리
    private void handlePut(String pathInfo, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        if (pathInfo != null && pathInfo.startsWith("/")) {
            Long postId = Long.parseLong(pathInfo.substring(1));
            JsonNode requestBody = readJsonRequest(req);
            String title = requestBody.get("title").asText();
            String content = requestBody.get("content").asText();

            boolean isUpdated = postsService.updatePost(postId, title, content);
            if (isUpdated) {
                sendJsonResponse(resp, HttpServletResponse.SC_OK, createJsonResponse("Post updated successfully"));
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Post not found");
            }
        } else {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid PUT request path");
        }
    }

    // DELETE 요청 처리
    private void handleDelete(String pathInfo, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        if (pathInfo != null && pathInfo.startsWith("/")) {
            Long postId = Long.parseLong(pathInfo.substring(1));
            boolean isDeleted = postsService.deletePost(postId);
            if (isDeleted) {
                sendJsonResponse(resp, HttpServletResponse.SC_OK, createJsonResponse("Post deleted successfully"));
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "Post not found");
            }
        } else {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid DELETE request path");
        }
    }

    private JsonNode readJsonRequest(HttpServletRequest req) throws IOException {
        String contentType = req.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            try (BufferedReader reader = req.getReader()) {
                return objectMapper.readTree(reader);
            }
        } else {
            throw new IOException("Content-Type is not application/json");
        }
    }

    private void sendJsonResponse(HttpServletResponse resp, int statusCode, JsonNode jsonResponse) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(jsonResponse.toString());
        }
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
