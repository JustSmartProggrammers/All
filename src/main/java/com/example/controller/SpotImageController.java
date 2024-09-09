package com.example.controller;

import com.example.service.SpotImageService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/v1/spot/image/*")
public class SpotImageController extends HttpServlet {

    private SpotImageService spotImageService = new SpotImageService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // "/gateball" 또는 "/" 경로가 올 수 있음
        String spot = (pathInfo != null && pathInfo.length() > 1) ? pathInfo.substring(1) : ""; // "gateball" 또는 ""

        // 서비스에서 이미지 경로 가져오기
        String imagePath = spotImageService.getImagePath(spot);

        // JSON으로 응답 생성
        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("image", imagePath);

        // Gson을 사용하여 JSON 응답
        Gson gson = new Gson();
        String json = gson.toJson(jsonResponse);

        // 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}

