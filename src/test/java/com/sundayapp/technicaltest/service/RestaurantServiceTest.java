package com.sundayapp.technicaltest.service;

import com.sundayapp.technicaltest.properties.RestaurantProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantProperties properties;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void testFoundRestaurantWithID() {
        List<RestaurantProperties.Restaurant> mockRestaurants = new ArrayList<>();
        RestaurantProperties.Restaurant mockRestaurant = mock(RestaurantProperties.Restaurant.class);
        when(mockRestaurant.getId()).thenReturn("1");
        when(mockRestaurant.getName()).thenReturn("Buffalo Montesson");
        when(mockRestaurant.getAddress()).thenReturn("3-11 Avenue Gabriel Péri, Montesson 78360, France");
        mockRestaurants.add(mockRestaurant);
        when(properties.getRestaurants()).thenReturn(mockRestaurants);

        Optional<RestaurantProperties.Restaurant> restaurant = restaurantService.findRestaurant("1");

        Assertions.assertTrue(restaurant.isPresent());
        Assertions.assertEquals(restaurant.get().getId(), "1");
        Assertions.assertEquals(restaurant.get().getName(), "Buffalo Montesson");
        Assertions.assertEquals(restaurant.get().getAddress(), "3-11 Avenue Gabriel Péri, Montesson 78360, France");
    }

    @Test
    void testNotFoundRestaurantWithID() {
        Optional<RestaurantProperties.Restaurant> restaurant = restaurantService.findRestaurant("2");

        Assertions.assertFalse(restaurant.isPresent());
    }
}