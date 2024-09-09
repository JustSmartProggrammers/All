package com.example.dao;

import com.example.model.Spot;

import java.util.List;

public interface SpotDAO {
    List<Spot> findAllSpots();
    List<Spot> findSpotsByRegion(String regionType);
    List<Spot> findSpotsBySportsType(String sportsType);
    Spot findSpotById(Long spotId);
    String findReservationSiteBySportsId(Long sportsId);
    List<Spot> findSpotsByRegionAndSportsType(String regionType, String sportsType);

}
