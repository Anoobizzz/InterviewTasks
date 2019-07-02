package com.etrusted.interview.demo;

import com.etrusted.interview.demo.entity.Order;
import com.etrusted.interview.demo.entity.Shop;
import com.etrusted.interview.demo.entity.User;
import com.etrusted.interview.demo.rest.dto.OrderResponse;
import com.etrusted.interview.demo.services.OrderManager;
import com.etrusted.interview.demo.services.RequestProcessingService;
import com.etrusted.interview.demo.services.ShopManager;
import com.etrusted.interview.demo.services.UserManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Hashtable;
import java.util.Map;

import static com.etrusted.interview.demo.FunctionalTests.EMAIL_1;
import static com.etrusted.interview.demo.FunctionalTests.EMAIL_2;
import static com.etrusted.interview.demo.FunctionalTests.createOrderRequest;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RequestProcessingService.class, OrderManager.class, ShopManager.class, UserManager.class})
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class UnitTests {
    @Autowired
    private RequestProcessingService service;
    @Autowired
    private UserManager userManager;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private ShopManager shopManager;
    @Spy
    private Map<String, User> users = new Hashtable<>();
    @Spy
    private Map<String, Shop> shops = new Hashtable<>();
    @Spy
    private Map<Long, Order> orders = new Hashtable<>();

    @Before
    public void before() {
        setField(userManager, "users", users);
        setField(shopManager, "shops", shops);
        setField(orderManager, "orders", orders);
    }

    @Test
    public void processOrder() {
        ResponseEntity<OrderResponse> response = service.processOrder(createOrderRequest(EMAIL_1));
        assertEquals(OK, response.getStatusCode());
        assertEquals(1, users.size());
        assertEquals(1, shops.size());
        assertEquals(1, orders.size());
    }

    @Test
    public void unsubscribe() {
        service.processOrder(createOrderRequest(EMAIL_1));
        ResponseEntity<User> response = service.unsubscribe(EMAIL_1);
        assertEquals(OK, response.getStatusCode());
        assertEquals(0, users.size());
        assertEquals(1, shops.size());
        assertEquals(1, orders.size());
        assertEquals("CCC6EB5FEC80580D76AA19A0C0E095E4C435C5273BDF491ABF406F309ABB5DA2",response.getBody().getEmail());
    }

    @Test
    public void getOrderById() {
        Long orderId = service.processOrder(createOrderRequest(EMAIL_1)).getBody().getId();
        ResponseEntity<Order> response = service.getOrderById(orderId);
        assertEquals(OK, response.getStatusCode());
        assertEquals(orderId, response.getBody().getId());
    }

    @Test
    public void processTwoOrdersWithSameUser() {
        ResponseEntity<OrderResponse> response = service.processOrder(createOrderRequest(EMAIL_1));
        assertEquals(OK, response.getStatusCode());
        assertEquals(1, users.size());
        assertEquals(1, shops.size());
        assertEquals(1, orders.size());
        response = service.processOrder(createOrderRequest(EMAIL_1));
        assertEquals(OK, response.getStatusCode());
        assertEquals(1, users.size());
        assertEquals(1, shops.size());
        assertEquals(2, orders.size());
    }

    @Test
    public void processTwoOrdersWithDifferentUsers() {
        ResponseEntity<OrderResponse> response = service.processOrder(createOrderRequest(EMAIL_1));
        assertEquals(OK, response.getStatusCode());
        assertEquals(1, users.size());
        assertEquals(1, shops.size());
        assertEquals(1, orders.size());
        response = service.processOrder(createOrderRequest(EMAIL_2));
        assertEquals(OK, response.getStatusCode());
        assertEquals(2, users.size());
        assertEquals(1, shops.size());
        assertEquals(2, orders.size());
    }
}
