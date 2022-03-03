package com.sundayapp.technicaltest.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {
    @EqualsAndHashCode.Include
    String productId;
    String name;
    Integer quantity;
    Double subTotal;
    OrderStatus status;
    Integer paidQuantity;

    public void mergeWithExistingOrder(Set<Order> existingOrders) {
        Optional<Order> matchedOrder = existingOrders.stream().filter(existingOrder -> existingOrder.getProductId().equalsIgnoreCase(getProductId())).findFirst();
        if (matchedOrder.isPresent()){
            Order mergedOrder = matchedOrder.get().toBuilder().quantity(matchedOrder.get().getQuantity() + getQuantity())
                    .subTotal(matchedOrder.get().getSubTotal() + getSubTotal()).build();
            if ((OrderStatus.PAID.equals(matchedOrder.get().getStatus()) || OrderStatus.PAID.equals(this.getStatus())) && !this.getStatus().equals(mergedOrder.getStatus())) {
                mergedOrder.setStatus(OrderStatus.PARTIALLY_PAID);
            }
            existingOrders.remove(matchedOrder.get());
            existingOrders.add(mergedOrder);
        }
        else {
            existingOrders.add(this);
        }
    }

    public void updateStatus(OrderStatus status) {
        this.setStatus(status);
        this.setPaidQuantity(this.getQuantity());
    }
}
