package com.sundayapp.technicaltest.model.paypal;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class PaypalOrder {
    private String reservationId;
    private String description;
}
