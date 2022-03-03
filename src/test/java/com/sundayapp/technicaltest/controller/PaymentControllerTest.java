package com.sundayapp.technicaltest.controller;

import com.sundayapp.technicaltest.SundayAppRepository;
import com.sundayapp.technicaltest.model.Order;
import com.sundayapp.technicaltest.model.PaypalPayment;
import com.sundayapp.technicaltest.model.Reservation;
import com.sundayapp.technicaltest.model.ReservationStatus;
import com.sundayapp.technicaltest.properties.MenuProperties;
import com.sundayapp.technicaltest.service.MenuService;
import com.sundayapp.technicaltest.service.PaypalService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaypalService paypalService;

    @InjectMocks
    private PaymentController controller;

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
                .currency("EUR")
                .status(ReservationStatus.INIT)
                .build();
    }

    @Test
    void testPayReservationWithCorrectReservationID() {
        Model model = mock(Model.class);

        String redirect = controller.payReservation("reservationId", model);

        verify(model).addAttribute("reservationId", "reservationId");
        verify(model).addAttribute("total", 27.79);
        verify(model).addAttribute("currency", "EUR");
        verify(model).addAttribute("reservation", SundayAppRepository.getInstance().getReservationMap().get("reservationId"));

        Assertions.assertEquals(redirect, "payWholeReservation.html");
    }

    @Test
    void testPayReservationWithPaidReservation() {
        Model model = mock(Model.class);

        Reservation reservation = SundayAppRepository.getInstance().getReservationMap().get("reservationId");
        PaypalPayment payments = PaypalPayment.builder().paymentId("paymentId").payerId("payerId").build();
        reservation.getPaypalPayments().add(payments);
        reservation.setStatus(ReservationStatus.PAID);

        String redirect = controller.payReservation("reservationId", model);

        verify(model).addAttribute("payments", reservation.getPaypalPayments());

        Assertions.assertEquals(redirect, "reservationPaid.html");
    }

    @Test
    void testPayReservationWithWrongReservationID() {
        Model model = mock(Model.class);

        String redirect = controller.payReservation("wrongReservationId", model);

        verifyNoInteractions(model);

        Assertions.assertEquals(redirect, "reservationError.html");
    }
}