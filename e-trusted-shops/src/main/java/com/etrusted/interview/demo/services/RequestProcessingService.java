package com.etrusted.interview.demo.services;

import com.etrusted.interview.demo.entity.Order;
import com.etrusted.interview.demo.entity.Shop;
import com.etrusted.interview.demo.entity.User;
import com.etrusted.interview.demo.rest.dto.OrderRequest;
import com.etrusted.interview.demo.rest.dto.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Service
public class RequestProcessingService {
    @Autowired
    private UserManager userManager;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private ShopManager shopManager;

    public ResponseEntity<OrderResponse> processOrder(OrderRequest orderRequest) {
        OrderResponse response = new OrderResponse();
        try {
            Future<User> user = supplyAsync(() -> userManager.saveUserOrGetExisting(
                    orderRequest.getFirstName(),
                    orderRequest.getLastName(),
                    orderRequest.getEmail()));
            Future<Shop> shop = supplyAsync(() -> shopManager.saveShopOrGetExisting(orderRequest.getShopURL()));
            response.setId(orderManager.putOrder(orderRequest.getOrderReference(), user.get(), shop.get()));
            return ResponseEntity.ok(response);
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<User> unsubscribe(String email) {
        return ResponseEntity.ok(userManager.unsubscribe(email));
    }

    public ResponseEntity<Order> getOrderById(Long id) {
        return orderManager.getById(id);
    }
}
