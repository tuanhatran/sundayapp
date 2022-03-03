package com.sundayapp.technicaltest.service;

import com.sundayapp.technicaltest.properties.MenuProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @Mock
    private MenuProperties properties;

    @InjectMocks
    private MenuService menuService;

    @Test
    void testGetMenuOfRestaurant() {
        List<MenuProperties.Menu> mockMenus = new ArrayList<>();
        MenuProperties.Menu mockMenu = mock(MenuProperties.Menu.class);
        when(mockMenu.getRestaurantId()).thenReturn("1");

        List<MenuProperties.Product> mockProducts = new ArrayList<>();
        MenuProperties.Product mockProduct = mock(MenuProperties.Product.class);
        when(mockProduct.getProductId()).thenReturn("1");
        when(mockProduct.getName()).thenReturn("Steak Frites");
        when(mockProduct.getPrice()).thenReturn(8.99);
        when(mockProduct.getType()).thenReturn("Main dish");
        mockProducts.add(mockProduct);

        when(mockMenu.getProducts()).thenReturn(mockProducts);
        mockMenus.add(mockMenu);
        when(properties.getMenus()).thenReturn(mockMenus);

        List<MenuProperties.Product> products = menuService.menuOf("1");

        Assertions.assertEquals(products.size(), 1);
        Assertions.assertEquals(products.get(0).getProductId(), "1");
        Assertions.assertEquals(products.get(0).getName(), "Steak Frites");
        Assertions.assertEquals(products.get(0).getType(), "Main dish");
        Assertions.assertEquals(products.get(0).getPrice(), 8.99);
    }

    @Test
    void testNotFoundMenuOfRestaurantOne() {
        List<MenuProperties.Product> products = menuService.menuOf("1");

        Assertions.assertEquals(products.size(), 0);
    }


}