package com.sundayapp.technicaltest.properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = RestaurantProperties.class)
@TestPropertySource("classpath:properties/restaurants_test.yml")
class RestaurantPropertiesTest {
    @Autowired
    private RestaurantProperties properties;

    @Test
    void loadRestaurantProperties() {
        Assertions.assertEquals(properties.getRestaurants().size(), 6);
        // Test we could have restaurant with id 5
        Optional<RestaurantProperties.Restaurant> restaurantIdFive = properties.getRestaurants().stream().filter(restaurant -> restaurant.getId().equalsIgnoreCase("5")).findFirst();
        Assertions.assertTrue(restaurantIdFive.isPresent());
        // Test Restaurant details
        Assertions.assertEquals(restaurantIdFive.get().getId(), "5");
        Assertions.assertEquals(restaurantIdFive.get().getName(), "Kebab de la gare");
        Assertions.assertEquals(restaurantIdFive.get().getAddress(), "6 Rue Turgot, 78500 Sartrouville, France");
        Assertions.assertEquals(restaurantIdFive.get().getType(), "Casual");
        Assertions.assertEquals(restaurantIdFive.get().getCurrency(), "EUR");
    }
}