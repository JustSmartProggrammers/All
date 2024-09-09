package com.example.controller;

import com.example.model.Spot;
import com.example.service.SpotService;
import com.example.service.SpotServiceImpl;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/api/v1/spots/*")
public class SpotController extends HttpServlet {

    private SpotService spotService;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        spotService = new SpotServiceImpl();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        try {
            if (path == null || "/".equals(path)) {
                handleGetAllSpots(req, resp);
            } else {
                RouteHandler handler = getRouteHandler(path);
                handler.handle(req, resp);
            }
        } catch (IllegalArgumentException e) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }
    }

    private RouteHandler getRouteHandler(String path) {
        if (path.startsWith("/region/")) {
            return (req, resp) -> handleGetSpotsByRegion(req, resp, path.substring("/region/".length()));
        } else if (path.startsWith("/detail/")) {
            return (req, resp) -> handleGetSpotDetail(req, resp, path.substring("/detail/".length()));
        } else if (path.startsWith("/reservation/")) {
            return (req, resp) -> handleGetReservationSite(req, resp, extractId(path, "/reservation/"));
        } else if (path.startsWith("/sports/")) {
            return (req, resp) -> handleGetSpotsBySportsType(req, resp, path.substring("/sports/".length()));
        } else if (path.startsWith("/filter")) {
            return this::handleGetSpotsByRegionAndSportsType;
        } else {
            throw new IllegalArgumentException("Invalid path: " + path);
        }
    }

    private void handleGetSpotsByRegionAndSportsType(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String regionType = req.getParameter("region");
        String sportsType = req.getParameter("sports");

        System.out.println("Received parameters - region: " + regionType + ", sports: " + sportsType);  // 디버깅용 로그

        if (regionType == null || sportsType == null || regionType.isEmpty() || sportsType.isEmpty()) {
            sendErrorResponse(resp, HttpServletResponse.SC_BAD_REQUEST, "Both region and sports parameters are required and cannot be empty");
            return;
        }

        try {
            List<Spot> spots = spotService.getSpotsByRegionAndSportsType(regionType, sportsType);
            System.out.println("Found " + spots.size() + " spots");  // 디버깅용 로그
            sendJsonResponse(resp, spots);
        } catch (Exception e) {
            System.err.println("Error in handleGetSpotsByRegionAndSportsType: " + e.getMessage());  // 디버깅용 로그
            e.printStackTrace();
            sendErrorResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }


    private Long extractId(String path, String prefix) {
        try {
            return Long.parseLong(path.substring(prefix.length()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid ID in path: " + path);
        }
    }

    private void handleGetAllSpots(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Spot> spots = spotService.getAllSpots();
        sendJsonResponse(resp, spots);
    }

    private void handleGetSpotsByRegion(HttpServletRequest req, HttpServletResponse resp, String regionType) throws IOException {
        List<Spot> spots = spotService.getSpotsByRegion(regionType);
        sendJsonResponse(resp, spots);
    }

    private void handleGetSpotsBySportsType(HttpServletRequest req, HttpServletResponse resp, String sportsType) throws IOException {
        List<Spot> spots = spotService.getSpotsBySportsType(sportsType);
        sendJsonResponse(resp, spots);
    }

    private void handleGetSpotDetail(HttpServletRequest req, HttpServletResponse resp, String spotIdStr) throws IOException {
        Long spotId = Long.parseLong(spotIdStr);
        Spot spot = spotService.getSpotDetail(spotId);
        sendJsonResponse(resp, spot);
    }

    private void handleGetReservationSite(HttpServletRequest req, HttpServletResponse resp, Long sportsId) throws IOException {
        String reservationSite = spotService.getReservationSite(sportsId);
        sendJsonResponse(resp, Collections.singletonMap("reservationSite", reservationSite));
    }

    private void sendJsonResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(data));
    }

    private void sendErrorResponse(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        sendJsonResponse(resp, Collections.singletonMap("error", message));
    }

    @FunctionalInterface
    private interface RouteHandler {
        void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
    }
}