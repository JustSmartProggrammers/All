package com.example.service;

import com.example.dao.SpotDAO;
import com.example.dao.SpotDAOImpl;
import com.example.model.Spot;

import java.util.List;

public class SpotServiceImpl implements SpotService {

    private SpotDAO spotDao;

    public SpotServiceImpl() {
        this.spotDao = new SpotDAOImpl(); // 실제로는 DI 컨테이너나 팩토리 패턴을 사용할 수 있음
    }

    @Override
    public List<Spot> getAllSpots() {
        return spotDao.findAllSpots();
    }

    @Override
    public List<Spot> getSpotsByRegion(String regionType) {
        return spotDao.findSpotsByRegion(regionType);
    }

    @Override
    public List<Spot> getSpotsBySportsType(String sportsType) {
        return spotDao.findSpotsBySportsType(sportsType);
    }


    @Override
    public Spot getSpotDetail(Long spotId) {
        return spotDao.findSpotById(spotId);
    }

    @Override
    public String getReservationSite(Long sportsId) {
        return spotDao.findReservationSiteBySportsId(sportsId);
    }

    @Override
    public List<Spot> getSpotsByRegionAndSportsType(String regionType, String sportsType) {
        return spotDao.findSpotsByRegionAndSportsType(regionType, sportsType);
    }
}
