package com.sundayapp.technicaltest.controller;

import com.sundayapp.technicaltest.model.Reservation;
import com.sundayapp.technicaltest.properties.MenuProperties;
import com.sundayapp.technicaltest.service.MenuService;
import com.sundayapp.technicaltest.service.QRCodeGenerationService;
import com.sundayapp.technicaltest.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@RestController
public class ReservationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

    @Value("${address}")
    private String serverAddress;

    private final QRCodeGenerationService qrCodeService;
    private final ReservationService reservationService;
    private final MenuService menuService;

    @Autowired
    public ReservationController(QRCodeGenerationService qrCodeService, ReservationService reservationService, MenuService menuService) {
        this.reservationService = reservationService;
        this.qrCodeService = qrCodeService;
        this.menuService = menuService;
    }

    @GetMapping(path = "/welcome")
    public String welcome(){
        return "Welcome to SundayApp";
    }

    /**
     * This controller is to create a QR code for new customer enter a restaurant with restaurantId
     * @param restaurantId
     * @param tableId
     * @return QRCode for client to make their Reservation
     */
    @GetMapping(path = "/newReservation", produces = MediaType.IMAGE_PNG_VALUE)
    public BufferedImage newReservation(@RequestParam("restaurantId") String restaurantId, @RequestParam("tableId") String tableId) {
        String newReservationId = reservationService.newReservation(restaurantId, tableId);
        LOGGER.info("Generated newReservationID: " + newReservationId);
        List<MenuProperties.Product> products = menuService.menuOf(restaurantId);
        String reservationLink = String.format("%s/reservation.html?restaurantId=%s&tableId=%s&reservationId=%s&currency=%s", serverAddress, restaurantId, tableId, newReservationId, reservationService.currencyOf(newReservationId));
        LOGGER.info("Reservation reservationLink: " + reservationLink);
        String paymentLink = String.format("%s/payWholeReservation?reservationId=%s", serverAddress, newReservationId);
        LOGGER.info("Payment reservationLink: " + paymentLink);
        return qrCodeService.generateEAN13BarcodeImage(reservationLink);
    }

    /**
     * This controller is update a Reservation, we could say that it's for the first update (when customer choose their dishes)
     * or for adding orders (drinks) and a lot of other changes
     * @param inputReservation : reservation to be updated
     * @return
     */
        @PostMapping(path = "/updateReservation")
    public String updateReservation(@RequestBody Reservation inputReservation) {
        reservationService.updateReservation(inputReservation);
        return "Your order has been updated, if you want to modify your order, don't hesitate";
    }


}
