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
@EnableConfigurationProperties(value = MenuProperties.class)
@TestPropertySource("classpath:properties/menus_test.yml")
class MenuPropertiesTest {

    @Autowired
    private MenuProperties menuProperties;

    @Test
    void loadMenuProperties(){
        Assertions.assertEquals(menuProperties.getMenus().size(), 2);
        // Test restaurantId
        Optional<MenuProperties.Menu> menuForRestaurantOne = menuProperties.getMenus().stream().filter(menu -> menu.getRestaurantId().equalsIgnoreCase("1")).findFirst();
        Assertions.assertTrue(menuForRestaurantOne.isPresent());
        // Test Products
        List<MenuProperties.Product> products = menuForRestaurantOne.get().getProducts();
        Assertions.assertEquals(products.size(), 12);
        Optional<MenuProperties.Product> productIdOne = products.stream().filter(product -> product.getProductId().equalsIgnoreCase("1")).findFirst();
        Assertions.assertTrue(productIdOne.isPresent());
        Assertions.assertEquals(productIdOne.get().getProductId(), "1");
        Assertions.assertEquals(productIdOne.get().getName(), "Carpaccio");
        Assertions.assertEquals(productIdOne.get().getType(), "Starter");
        Assertions.assertEquals(productIdOne.get().getPrice(), 6.78);
    }
}