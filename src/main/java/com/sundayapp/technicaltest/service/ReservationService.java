package com.sundayapp.technicaltest.service;

import com.sundayapp.technicaltest.SundayAppRepository;
import com.sundayapp.technicaltest.model.Order;
import com.sundayapp.technicaltest.model.Reservation;
import com.sundayapp.technicaltest.model.ReservationStatus;
import com.sundayapp.technicaltest.properties.RestaurantProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ReservationService {

    private RestaurantService restaurantService;

    public ReservationService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    public void updateReservation(Reservation inputReservation) {
        String reservationId = inputReservation.getId();
        Map<String, Reservation> reservationMap = SundayAppRepository.getInstance().getReservationMap();
        Reservation reservation = reservationMap.get(reservationId);
        if (reservation != null) {
            Set<Order> existingOrders = reservation.getOrders();
            if (existingOrders.isEmpty()) { //if it's the first reservation
                reservation.setOrders(inputReservation.getOrders());
                reservation.setTotal(inputReservation.getTotal());
            } else {
                inputReservation.getOrders().forEach(order -> order.mergeWithExistingOrder(existingOrders));
                reservation.setTotal(reservation.getTotal() + inputReservation.getTotal());
            }
            reservationMap.put(reservationId, reservation);
        }
    }

    public String newReservation(String restaurantId, String tableId) {
        String newReservationID = UUID.randomUUID().toString();
        Optional<RestaurantProperties.Restaurant> foundRestaurant = restaurantService.findRestaurant(restaurantId);
        Reservation.ReservationBuilder reservationBuilder = Reservation.builder()
                .id(newReservationID)
                .dateTime(LocalDateTime.now())
                .orders(new HashSet<>())
                .tableId(tableId)
                .paypalPayments(new ArrayList<>())
                .status(ReservationStatus.INIT);
        if (foundRestaurant.isPresent()){
            reservationBuilder.restaurant(foundRestaurant.get());
            reservationBuilder.currency(foundRestaurant.get().getCurrency());
        }
        SundayAppRepository.getInstance().getReservationMap().put(newReservationID, reservationBuilder.build());
        return newReservationID;
    }

    public String currencyOf(String reservationId) {
        Reservation reservation = SundayAppRepository.getInstance().getReservationMap().get(reservationId);
        if (reservation != null){
            return reservation.getCurrency();
        }
        return "EUR"; // Default currency
    }
}
