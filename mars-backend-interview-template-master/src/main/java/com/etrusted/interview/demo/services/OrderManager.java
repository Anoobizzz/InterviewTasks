package com.etrusted.interview.demo.services;

import com.etrusted.interview.demo.entity.Order;
import com.etrusted.interview.demo.entity.Shop;
import com.etrusted.interview.demo.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

@Component
public class OrderManager {
    private Map<Long, Order> orders = new Hashtable<>();

    public Long putOrder(String reference, User user, Shop shop) {
        Order order = new Order(reference, user, shop);
        Long id = order.getId();
        orders.put(id, order);
        return id;
    }

    public ResponseEntity<Order> getById(Long id) {
        return ResponseEntity.ok(orders.get(id));
    }
}