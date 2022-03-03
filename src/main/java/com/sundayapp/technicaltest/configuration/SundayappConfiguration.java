package com.sundayapp.technicaltest.configuration;

import com.paypal.base.rest.APIContext;
import com.sundayapp.technicaltest.service.MenuService;
import com.sundayapp.technicaltest.service.PaypalService;
import com.sundayapp.technicaltest.service.QRCodeGenerationService;
import com.sundayapp.technicaltest.service.ReservationService;
import com.sundayapp.technicaltest.service.RestaurantService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;

@Configuration
public class SundayappConfiguration {

    @Value("${paypal.client.id}")
    private String clientId;
    @Value("${paypal.client.secret}")
    private String clientSecret;
    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public QRCodeGenerationService qrCodeGenerationService() {
        return new QRCodeGenerationService();
    }

    @Bean
    public HttpMessageConverter<BufferedImage> httpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean
    public RestaurantService restaurantService(){
        return new RestaurantService();
    }

    @Bean
    public ReservationService reservationService(){
        return new ReservationService(restaurantService());
    }

    @Bean
    public MenuService menuService(){
        return new MenuService();
    }

    @Bean
    public PaypalService paypalService(){ return new PaypalService();}

    @Bean
    public APIContext apiContext() {
        return new APIContext(clientId, clientSecret, mode);
    }
}
