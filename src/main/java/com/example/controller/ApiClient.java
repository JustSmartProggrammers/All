package com.example.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ApiClient {
    public static void main(String[] args) {
        try {
            // API Endpoint 예시
            String url = "http://localhost:8080/api/v1/posts/1/comments";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // 요청 방법을 GET으로 설정
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            // 응답 코드 가져오기
            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 응답 읽기
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 응답을 JSON으로 변환
            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println(jsonResponse.toString(2));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
