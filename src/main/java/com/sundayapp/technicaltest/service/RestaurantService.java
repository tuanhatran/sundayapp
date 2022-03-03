package com.sundayapp.technicaltest.service;

import com.sundayapp.technicaltest.properties.RestaurantProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class RestaurantService {

    @Autowired
    private RestaurantProperties properties;

    public Optional<RestaurantProperties.Restaurant> findRestaurant(String id) {
        return properties.getRestaurants().stream().filter(restaurant -> restaurant.getId().equalsIgnoreCase(id)).findFirst();
    }
}
