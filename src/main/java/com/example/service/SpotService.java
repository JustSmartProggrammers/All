package com.example.service;

import com.example.model.Spot;

import java.util.List;

public interface SpotService {
    List<Spot> getAllSpots();
    List<Spot> getSpotsByRegion(String regionType);
    List<Spot> getSpotsBySportsType(String sportsType);
    Spot getSpotDetail(Long spotId);
    String getReservationSite(Long sportsId);
    List<Spot> getSpotsByRegionAndSportsType(String regionType, String sportsType);

}
