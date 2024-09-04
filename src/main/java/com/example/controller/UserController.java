package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import com.example.config.JacksonConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/v1/user/*")
public class UserController extends HttpServlet {

    private UserService userService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
        objectMapper = JacksonConfig.getObjectMapper();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String method = req.getMethod();
            String pathInfo = req.getPathInfo();

            switch (method) {
                case "GET":
                    handleGet(pathInfo, req, resp);
                    break;
                case "PUT":
                    handlePut(pathInfo, req, resp);
                    break;
                default:
                    sendErrorResponse(resp, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method not allowed");
            }
        } catch (Exception e) {
            log("Unexpected error occurred: ", e);
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
        }
    }

    private void handleGet(String pathInfo, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        if (pathInfo == null || pathInfo.equals("/")) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
            return;
        }

        try {
            Long userId = Long.parseLong(pathInfo.substring(1));
            User user = userService.getUserById(userId);
            if (user != null) {
                sendJsonResponse(resp, HttpServletResponse.SC_OK, user);
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        }
    }

    private void handlePut(String pathInfo, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (pathInfo == null || pathInfo.equals("/")) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
            return;
        }

        try {
            Long userId = Long.parseLong(pathInfo.substring(1));
            JsonNode jsonRequest = readJsonRequest(req);
            User user = objectMapper.treeToValue(jsonRequest, User.class);
            user.setId(userId);

            User updatedUser = userService.updateUser(user);
            if (updatedUser != null) {
                sendJsonResponse(resp, HttpServletResponse.SC_OK, updatedUser);
            } else {
                sendErrorResponse(resp, HttpServletResponse.SC_NOT_FOUND, "User not found");
            }
        } catch (NumberFormatException e) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID");
        } catch (SQLException e) {
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error: " + e.getMessage());
        } catch (JsonProcessingException e) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format: " + e.getMessage());
        }
    }

    private JsonNode readJsonRequest(HttpServletRequest req) throws IOException {
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return objectMapper.readTree(sb.toString());
    }

    private void sendJsonResponse(HttpServletResponse resp, int statusCode, Object data) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(objectMapper.writeValueAsString(data));
    }

    private void sendErrorResponse(HttpServletResponse resp, int statusCode, String message) throws IOException {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        sendJsonResponse(resp, statusCode, errorResponse);
    }
}