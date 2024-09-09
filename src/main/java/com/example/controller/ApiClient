package com.example.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ApiClient {
    public static void main(String[] args) {
        try {
            // Example API endpoint URL
            String url = "http://localhost:8080/api/v1/posts/1/comments";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Set the request method to GET
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            // Get the response code
            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Convert the response to JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println(jsonResponse.toString(2));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
