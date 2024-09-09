package com.example.dao;

import com.example.model.Spot;
import com.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpotDAOImpl implements SpotDAO {

    @Override
    public List<Spot> findAllSpots() {
        List<Spot> spots = new ArrayList<>();
        String query = "SELECT * FROM spot";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Spot spot = new Spot();
                spot.setId(rs.getLong("id"));
                spot.setName(rs.getString("name"));
                spot.setAddress(rs.getString("address"));
                spot.setContact(rs.getString("contact"));
                spot.setReservation(rs.getString("reservation"));
                spot.setRegionType(rs.getString("regionType"));
                spot.setSportsType(rs.getString("sportsType"));
                spot.setWeekdayHours(rs.getString("weekdayHours"));
                spot.setWeekendHours(rs.getString("weekendHours"));
                spot.setFee(rs.getString("fee"));
                spot.setParking(rs.getString("parking"));
                spot.setRentalAvailable(rs.getString("rentalAvailable"));
                spots.add(spot);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }

    @Override
    public List<Spot> findSpotsByRegion(String regionType) {
        List<Spot> spots = new ArrayList<>();
        String query = "SELECT * FROM spot WHERE regionType = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, regionType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Spot spot = new Spot();
                    spot.setId(rs.getLong("id"));
                    spot.setName(rs.getString("name"));
                    spot.setAddress(rs.getString("address"));
                    spot.setContact(rs.getString("contact"));
                    spot.setReservation(rs.getString("reservation"));
                    spot.setRegionType(rs.getString("regionType"));
                    spot.setSportsType(rs.getString("sportsType"));
                    spot.setWeekdayHours(rs.getString("weekdayHours"));
                    spot.setWeekendHours(rs.getString("weekendHours"));
                    spot.setFee(rs.getString("fee"));
                    spot.setParking(rs.getString("parking"));
                    spot.setRentalAvailable(rs.getString("rentalAvailable"));
                    spots.add(spot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }

    @Override
    public List<Spot> findSpotsBySportsType(String sportsType) {
        List<Spot> spots = new ArrayList<>();
        String query = "SELECT * FROM spot WHERE sportsType = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, sportsType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Spot spot = new Spot();
                    spot.setId(rs.getLong("id"));
                    spot.setName(rs.getString("name"));
                    spot.setAddress(rs.getString("address"));
                    spot.setContact(rs.getString("contact"));
                    spot.setReservation(rs.getString("reservation"));
                    spot.setRegionType(rs.getString("regionType"));
                    spot.setSportsType(rs.getString("sportsType"));
                    spot.setWeekdayHours(rs.getString("weekdayHours"));
                    spot.setWeekendHours(rs.getString("weekendHours"));
                    spot.setFee(rs.getString("fee"));
                    spot.setParking(rs.getString("parking"));
                    spot.setRentalAvailable(rs.getString("rentalAvailable"));
                    spots.add(spot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spots;
    }

    @Override
    public Spot findSpotById(Long spotId) {
        Spot spot = null;
        String query = "SELECT * FROM spot WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, spotId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    spot = new Spot();
                    spot.setId(rs.getLong("id"));
                    spot.setName(rs.getString("name"));
                    spot.setAddress(rs.getString("address"));
                    spot.setContact(rs.getString("contact"));
                    spot.setReservation(rs.getString("reservation"));
                    spot.setRegionType(rs.getString("regionType"));
                    spot.setSportsType(rs.getString("sportsType"));
                    spot.setWeekdayHours(rs.getString("weekdayHours"));
                    spot.setWeekendHours(rs.getString("weekendHours"));
                    spot.setFee(rs.getString("fee"));
                    spot.setParking(rs.getString("parking"));
                    spot.setRentalAvailable(rs.getString("rentalAvailable"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return spot;
    }

    @Override
    public String findReservationSiteBySportsId(Long sportsId) {
        String reservationSite = null;
        String query = "SELECT reservation FROM spot WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, sportsId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reservationSite = rs.getString("reservation");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationSite;
    }
    @Override
    public List<Spot> findSpotsByRegionAndSportsType(String regionType, String sportsType) {
        List<Spot> spots = new ArrayList<>();
        String query = "SELECT * FROM spot WHERE regionType = ? AND sportsType = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, regionType);
            stmt.setString(2, sportsType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Spot spot = new Spot();
                    spot.setId(rs.getLong("id"));
                    spot.setName(rs.getString("name"));
                    spot.setAddress(rs.getString("address"));
                    spot.setContact(rs.getString("contact"));
                    spot.setReservation(rs.getString("reservation"));
                    spot.setRegionType(rs.getString("regionType"));
                    spot.setSportsType(rs.getString("sportsType"));
                    spot.setWeekdayHours(rs.getString("weekdayHours"));
                    spot.setWeekendHours(rs.getString("weekendHours"));
                    spot.setFee(rs.getString("fee"));
                    spot.setParking(rs.getString("parking"));
                    spot.setRentalAvailable(rs.getString("rentalAvailable"));
                    spots.add(spot);

                    // 디버깅을 위한 로그 추가
                    System.out.println("Found spot: " + spot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Error: " + e.getMessage());
        }
        return spots;
    }
}
