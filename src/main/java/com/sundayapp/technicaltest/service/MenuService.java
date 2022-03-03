package com.sundayapp.technicaltest.service;

import com.sundayapp.technicaltest.properties.MenuProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuService {

    @Autowired
    private MenuProperties menuProperties;

    public List<MenuProperties.Product> menuOf(String restaurantId) {
        Optional<MenuProperties.Menu> foundMenu = menuProperties.getMenus().stream().filter(menu -> menu.getRestaurantId().equalsIgnoreCase(restaurantId)).findFirst();
        if (foundMenu.isPresent()){
            return foundMenu.get().getProducts();
        }
        return new ArrayList<>();
    }
}
