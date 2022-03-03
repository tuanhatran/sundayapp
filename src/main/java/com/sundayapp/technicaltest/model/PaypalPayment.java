package com.sundayapp.technicaltest.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaypalPayment {
    private String paymentId;
    private String payerId;
}
