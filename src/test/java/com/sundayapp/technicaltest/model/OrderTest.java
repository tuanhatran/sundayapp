package com.sundayapp.technicaltest.model;

import com.sundayapp.technicaltest.SundayAppRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @BeforeEach
    void setUp() {
    }

    @Test
    void testMergeWithExistingOrder() {
        Order firstOrder = Order.builder().productId("1").name("Steak Frites").quantity(3).subTotal(18.00).build();
        Set<Order> orders = new HashSet<>();
        orders.add(firstOrder);
        Order secondOrder = Order.builder().productId("1").name("Steak Frites").quantity(2).subTotal(12.00).build();

        secondOrder.mergeWithExistingOrder(orders);

        Assertions.assertEquals(orders.size(), 1);
        Optional<Order> foundOrder = orders.stream().filter(order -> order.getProductId().equalsIgnoreCase("1")).findFirst();
        Assertions.assertTrue(foundOrder.isPresent());
        Assertions.assertEquals(foundOrder.get().getName(),"Steak Frites");
        Assertions.assertEquals(foundOrder.get().getQuantity(),5);
        Assertions.assertEquals(foundOrder.get().getSubTotal(),30.00);
    }

    @Test
    void testMergeWithNewOrder() {
        Order firstOrder = Order.builder().productId("1").name("Steak Frites").quantity(3).subTotal(18.00).build();
        Set<Order> orders = new HashSet<>();
        orders.add(firstOrder);
        Order secondOrder = Order.builder().productId("2").name("Burgeur").quantity(2).subTotal(12.00).build();

        secondOrder.mergeWithExistingOrder(orders);

        Assertions.assertEquals(orders.size(), 2);
        Optional<Order> foundOrder = orders.stream().filter(order -> order.getProductId().equalsIgnoreCase("2")).findFirst();
        Assertions.assertTrue(foundOrder.isPresent());
        Assertions.assertEquals(foundOrder.get(), secondOrder);
    }

    @Test
    void testUpdateStatus(){
        Order firstOrder = Order.builder().productId("1").name("Steak Frites").quantity(3).subTotal(18.00).status(OrderStatus.INIT).build();

        firstOrder.updateStatus(OrderStatus.PAID);

        Assertions.assertEquals(firstOrder.getStatus(), OrderStatus.PAID);
    }

    @Test
    void testMergedOrderWithPaidOrder(){
        Order firstOrder = Order.builder().productId("1").name("Steak Frites").quantity(3).subTotal(18.00).status(OrderStatus.INIT).build();
        firstOrder.updateStatus(OrderStatus.PAID);
        Set<Order> orders = new HashSet<>();
        orders.add(firstOrder);

        Order secondOrder = Order.builder().productId("1").name("Steak Frites").quantity(2).subTotal(12.00).status(OrderStatus.INIT).build();

        secondOrder.mergeWithExistingOrder(orders);

        Assertions.assertEquals(orders.size(),1);
        Optional<Order> foundOrder = orders.stream().filter(order -> order.getProductId().equalsIgnoreCase("1")).findFirst();
        Assertions.assertTrue(foundOrder.isPresent());
        Assertions.assertEquals(foundOrder.get().getQuantity(), 5);
        Assertions.assertEquals(foundOrder.get().getPaidQuantity(), 3);
        Assertions.assertEquals(foundOrder.get().getStatus(), OrderStatus.PARTIALLY_PAID);
        Assertions.assertEquals(foundOrder.get().getSubTotal(), 30.00);

    }
}