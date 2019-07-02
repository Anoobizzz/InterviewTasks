package com.etrusted.interview.demo;

import com.etrusted.interview.demo.entity.Order;
import com.etrusted.interview.demo.entity.User;
import com.etrusted.interview.demo.rest.dto.OrderRequest;
import com.etrusted.interview.demo.rest.dto.OrderResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FunctionalTests {
    public static final String EMAIL_1 = "asd@ads.com";
    public static final String EMAIL_2 = "asd1@ads.com";
    public static final String EMAIL_3 = "asd2@ads.com";
    private static final String LOCAL_LINK = "http://localhost:";
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void putOrder() {
        ResponseEntity<OrderResponse> responseEntity = restTemplate.exchange(LOCAL_LINK + port + "/createOrder",
                HttpMethod.PUT, createOrderRequestEntity(EMAIL_1), OrderResponse.class);
        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody().getId());
    }

    @Test
    public void putOrderTwiceSameUser() throws ExecutionException, InterruptedException {
        Future<ResponseEntity<OrderResponse>> response1 = supplyAsync(() -> restTemplate.exchange(LOCAL_LINK + port + "/createOrder",
                HttpMethod.PUT, createOrderRequestEntity(EMAIL_1), OrderResponse.class));
        Future<ResponseEntity<OrderResponse>> response2 = supplyAsync(() -> restTemplate.exchange(LOCAL_LINK + port + "/createOrder",
                HttpMethod.PUT, createOrderRequestEntity(EMAIL_1), OrderResponse.class));
        assertEquals(OK, response1.get().getStatusCode());
        assertNotNull(response1.get().getBody().getId());
        assertEquals(OK, response2.get().getStatusCode());
        assertNotNull(response2.get().getBody().getId());
        assertNotEquals(response1.get().getBody().getId(), response2.get().getBody().getId());
    }

    @Test
    public void putOrderTwiceDifferentUsers() {
        ResponseEntity<OrderResponse> response1 = restTemplate.exchange(LOCAL_LINK + port + "/createOrder",
                HttpMethod.PUT, createOrderRequestEntity(EMAIL_1), OrderResponse.class);
        assertEquals(OK, response1.getStatusCode());
        assertNotNull(response1.getBody().getId());
        ResponseEntity<OrderResponse> response2 = restTemplate.exchange(LOCAL_LINK + port + "/createOrder",
                HttpMethod.PUT, createOrderRequestEntity(EMAIL_2), OrderResponse.class);
        assertEquals(OK, response2.getStatusCode());
        assertNotNull(response2.getBody().getId());
        assertNotEquals(response1.getBody().getId(), response2.getBody().getId());
    }

    @Test
    public void putOrderAndUnsubscribe() {
        ResponseEntity<OrderResponse> orderResponse = restTemplate.exchange(LOCAL_LINK + port + "/createOrder",
                HttpMethod.PUT, createOrderRequestEntity(EMAIL_1), OrderResponse.class);
        assertEquals(OK, orderResponse.getStatusCode());
        assertNotNull(orderResponse.getBody().getId());
        ResponseEntity<User> unsubscribeResponse = restTemplate.exchange(LOCAL_LINK + port + "/unsubscribe",
                HttpMethod.DELETE, createUnsubscribeRequest(EMAIL_1), User.class);
        assertEquals(OK, orderResponse.getStatusCode());
        assertEquals("CCC6EB5FEC80580D76AA19A0C0E095E4C435C5273BDF491ABF406F309ABB5DA2", unsubscribeResponse.getBody().getEmail());
    }

    @Test
    public void putOrderAndGetById() {
        ResponseEntity<OrderResponse> orderResponse = restTemplate.exchange(LOCAL_LINK + port + "/createOrder",
                HttpMethod.PUT, createOrderRequestEntity(EMAIL_3), OrderResponse.class);
        assertEquals(OK, orderResponse.getStatusCode());
        assertNotNull(orderResponse.getBody().getId());
        ResponseEntity<Order> getResponse = restTemplate.exchange(LOCAL_LINK + port + "/order/" + orderResponse.getBody().getId(),
                HttpMethod.GET, createEmptyRequest(), Order.class);
        assertEquals(OK, getResponse.getStatusCode());
        assertEquals("6C4D9ACC7E7479D12EBA426455CEA155172B95B97D02E32D90186A43CCF64F24", getResponse.getBody().getUser().getEmail());
    }

    @Test
    public void putEmptyOrder() {
        ResponseEntity<OrderResponse> orderResponse = restTemplate.exchange(LOCAL_LINK + port + "/createOrder",
                HttpMethod.PUT, createEmptyRequest(), OrderResponse.class);
        assertEquals(BAD_REQUEST, orderResponse.getStatusCode());
    }

    private HttpEntity<OrderRequest> createOrderRequestEntity(String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(createOrderRequest(email), headers);
    }

    private HttpEntity createUnsubscribeRequest(String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(email, headers);
    }

    private HttpEntity createEmptyRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(null, headers);
    }

    public static OrderRequest createOrderRequest(String email) {
        OrderRequest request = new OrderRequest();
        request.setEmail(email);
        request.setFirstName("name");
        request.setLastName("surname");
        request.setOrderReference("orderReference");
        request.setShopURL("http://shopurl.com");
        return request;
    }
}
