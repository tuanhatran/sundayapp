package com.sundayapp.technicaltest;

import com.sundayapp.technicaltest.model.Order;
import com.sundayapp.technicaltest.model.PaypalPayment;
import com.sundayapp.technicaltest.model.Reservation;
import com.sundayapp.technicaltest.model.ReservationStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SundayAppRepositoryTest {

    @BeforeEach
    void setUp() {
        Reservation reservation = aReservation("reservationId");

        SundayAppRepository.getInstance().getReservationMap().put("reservationId", reservation);
    }

    private Reservation aReservation(String reservationId) {
        HashSet<Order> orders = new HashSet<>();
        Order anOrder = Order.builder().name("Steak Frites").productId("1").quantity(3).subTotal(27.79).build();
        orders.add(anOrder);

        return Reservation.builder()
                .id(reservationId)
                .orders(orders)
                .tableId("14")
                .paypalPayments(new ArrayList<>())
                .total(27.79)
                .status(ReservationStatus.INIT)
                .build();
    }

    @Test
    void testRepositoryExistingReservation(){
        Map<String, Reservation> reservationMap = SundayAppRepository.getInstance().getReservationMap();

        Assertions.assertFalse(reservationMap.isEmpty());
        Assertions.assertEquals(reservationMap.size(), 1);
        Reservation reservation = reservationMap.get("reservationId");
        assertNotNull(reservation);
        Assertions.assertEquals(reservation.getTableId(), "14");
        Assertions.assertTrue(reservation.getPaypalPayments().isEmpty());
        Assertions.assertEquals(reservation.getTotal(), 27.79);
        Assertions.assertEquals(reservation.getStatus(), ReservationStatus.INIT);

        //Test Order
        Assertions.assertEquals(reservation.getOrders().size(), 1);
        Optional<Order> orderOne = reservation.getOrders().stream().filter(order -> order.getProductId().equalsIgnoreCase("1")).findFirst();
        Assertions.assertTrue(orderOne.isPresent());
        Assertions.assertEquals(orderOne.get().getName(), "Steak Frites");
        Assertions.assertEquals(orderOne.get().getQuantity(), 3);
        Assertions.assertEquals(orderOne.get().getSubTotal(), 27.79);
    }

    @Test
    void testUpdateReservationIntoRepository(){
        Map<String, Reservation> reservationMap = SundayAppRepository.getInstance().getReservationMap();
        Reservation reservation = reservationMap.get("reservationId");
        assertNotNull(reservation);
        reservation.getPaypalPayments().add(PaypalPayment.builder().paymentId("paymentId").payerId("payerId").build());
        reservation.setStatus(ReservationStatus.PAID);

        reservationMap = SundayAppRepository.getInstance().getReservationMap();
        reservation = reservationMap.get("reservationId");

        Assertions.assertEquals(reservation.getPaypalPayments().size(), 1);
        Assertions.assertEquals(reservation.getPaypalPayments().get(0).getPaymentId(), "paymentId");
        Assertions.assertEquals(reservation.getPaypalPayments().get(0).getPayerId(), "payerId");
        Assertions.assertEquals(reservation.getStatus(), ReservationStatus.PAID);
    }

    @Test
    void testAddNewReservationIntoRepository(){
        Map<String, Reservation> reservationMap = SundayAppRepository.getInstance().getReservationMap();
        Reservation newReservation = aReservation("newReservationId");
        reservationMap.put("newReservationId", newReservation);

        reservationMap = SundayAppRepository.getInstance().getReservationMap();

        Assertions.assertEquals(reservationMap.size(), 2);
    }
}