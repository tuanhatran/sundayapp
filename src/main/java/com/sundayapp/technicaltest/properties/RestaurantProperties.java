package com.sundayapp.technicaltest.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource(value = "classpath:properties/restaurants.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties("restaurant")
@Data
public class RestaurantProperties {
    private List<Restaurant> restaurants = new ArrayList<>();

    @Data
    public static class Restaurant {
        private String id;
        private String name;
        private String type;
        private String currency;
        private String address;
    }
}
