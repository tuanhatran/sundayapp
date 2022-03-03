package com.sundayapp.technicaltest.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.sundayapp.technicaltest.SundayAppRepository;
import com.sundayapp.technicaltest.model.PaypalPayment;
import com.sundayapp.technicaltest.model.Reservation;
import com.sundayapp.technicaltest.model.ReservationStatus;
import com.sundayapp.technicaltest.model.paypal.PaypalOrder;
import com.sundayapp.technicaltest.service.PaypalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    public static final String SUCCESS_URL = "payPaypal/success";
    public static final String CANCEL_URL = "payPaypal/cancel";

    @Value("${address}")
    private String serverAddress;

    @Autowired
    PaypalService paypalService;

    @RequestMapping("/payWholeReservation")
    public String payReservation(@RequestParam("reservationId") String reservationId, Model model) {
        Reservation reservation = SundayAppRepository.getInstance().getReservationMap().get(reservationId);
        if (reservation != null) {
            if (!ReservationStatus.PAID.equals(reservation.getStatus())) {
                model.addAttribute("reservationId", reservationId);
                model.addAttribute("total", reservation.getTotal());
                model.addAttribute("currency", reservation.getCurrency());
                model.addAttribute("reservation", reservation);
                return "payWholeReservation.html";
            } else {
                model.addAttribute("payments", reservation.getPaypalPayments());
                return "reservationPaid.html";
            }
        }
        return "reservationError.html";
    }

    @PostMapping("/payPaypal")
    public String payment(@ModelAttribute("order") PaypalOrder order) {
        String reservationId = order.getReservationId();
        Reservation reservation = SundayAppRepository.getInstance().getReservationMap().get(reservationId);
        if (reservation != null && !ReservationStatus.PAID.equals(reservation.getStatus())) { //If reservation exist and not paid
            try {
                Payment payment = paypalService.createPayment(reservation.getTotal(),
                        reservation.getCurrency(),
                        "Paypal",
                        "SALE",
                        order.getDescription(),
                        String.format("%s/%s?reservationId=%s", serverAddress, CANCEL_URL, reservationId),
                        String.format("%s/%s?reservationId=%s", serverAddress, SUCCESS_URL, reservationId));
                for (Links link : payment.getLinks()) {
                    if (link.getRel().equals("approval_url")) {
                        return "redirect:" + link.getHref();
                    }
                }
            } catch (PayPalRESTException e) {
                LOGGER.error("Error while trying to create payment : " + e.getMessage());
                return "reservationError.html";
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("reservationId") String reservationId, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        Reservation reservation = SundayAppRepository.getInstance().getReservationMap().get(reservationId);
        if (reservation != null) {
            reservation.setStatus(ReservationStatus.PAID);
            reservation.getPaypalPayments().add(PaypalPayment.builder().paymentId(paymentId).payerId(payerId).build());
            try {
                Payment payment = paypalService.executePayment(paymentId, payerId);
                LOGGER.info(payment.toJSON());
                if (payment.getState().equals("approved")) {
                    return "success";
                }
            } catch (PayPalRESTException e) {
                LOGGER.error("Error while trying to execute payment : " + e.getMessage());
            }
        }

        return "redirect:/";
    }
}
