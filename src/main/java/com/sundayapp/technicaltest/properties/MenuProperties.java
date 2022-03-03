package com.sundayapp.technicaltest.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@PropertySource(value = "classpath:properties/menus.yml", factory = YamlPropertySourceFactory.class)
@ConfigurationProperties("menu")
@Data
public class MenuProperties {
    private List<Menu> menus = new ArrayList<>();

    @Data
    public static class Menu {
        private String restaurantId;
        private List<Product> products = new ArrayList<>();
    }

    @Data
    public static class Product {
        private String productId;
        private String name;
        private String type;
        private Double price;
    }
}
