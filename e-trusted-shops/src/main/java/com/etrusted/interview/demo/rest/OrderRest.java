package com.etrusted.interview.demo.rest;

import com.etrusted.interview.demo.entity.Order;
import com.etrusted.interview.demo.entity.User;
import com.etrusted.interview.demo.rest.dto.OrderRequest;
import com.etrusted.interview.demo.rest.dto.OrderResponse;
import com.etrusted.interview.demo.services.RequestProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(consumes = "application/json")
public class OrderRest {
    @Autowired
    private RequestProcessingService service;

    @RequestMapping(value = "/createOrder", method = RequestMethod.PUT)
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        return service.processOrder(orderRequest);
    }

    @RequestMapping(value = "/unsubscribe", method = RequestMethod.DELETE)
    public ResponseEntity<User> unsubscribe(@RequestBody String email) {
        return service.unsubscribe(email);
    }

    @RequestMapping(value = "/order/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return service.getOrderById(id);
    }


    @RequestMapping("/test1")
    public String test1(){
        return "test1";
    }

    @RequestMapping("/test2")
    public String test2(){
        return "test2";
    }

    @RequestMapping("/test/test3")
    public String test3(){
        return "test3";
    }

    @RequestMapping("/test/test4")
    public String test4(){
        return "test4";
    }
}
