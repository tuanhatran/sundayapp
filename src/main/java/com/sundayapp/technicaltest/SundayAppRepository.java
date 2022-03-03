package com.sundayapp.technicaltest;

import com.sundayapp.technicaltest.model.Reservation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SundayAppRepository {
    private static SundayAppRepository repositoryInstance = null;

    private Map<String, Reservation> reservationMap = new ConcurrentHashMap<>();

    private SundayAppRepository(){};

    public static SundayAppRepository getInstance(){
        if (repositoryInstance == null) {
            repositoryInstance = new SundayAppRepository();
        }
        return repositoryInstance;
    }

    public Map<String, Reservation> getReservationMap() {
        return reservationMap;
    }
}
