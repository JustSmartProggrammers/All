package com.example.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import org.json.JSONObject;

public class ApiClient {
    public static void main(String[] args) {
        try {
            // API endpoint 예시
            String urlString = "http://localhost:8080/api/v1/posts/1/comments";

            // URL 문자열에서 URI 개체 만듬
            URI uri = new URI(urlString);

            // URI를 URL로 변환
            URL url = uri.toURL();

            // Open connection
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // 요청 방법 GET으로 설정
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

        } catch (URISyntaxException e) {
            System.err.println("Invalid URL syntax: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
