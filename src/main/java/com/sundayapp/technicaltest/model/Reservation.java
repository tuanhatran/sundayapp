package com.sundayapp.technicaltest.model;

import com.sundayapp.technicaltest.properties.RestaurantProperties;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Reservation {
    private String id;
    private RestaurantProperties.Restaurant restaurant;
    private String tableId;
    private Set<Order> orders;
    private Double total;
    private String currency;
    private LocalDateTime dateTime;
    private ReservationStatus status;
    private List<PaypalPayment> paypalPayments;
}
