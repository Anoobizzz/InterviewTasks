package com.etrusted.interview.demo.entity;

public class Order {
    private static Long counter = 0L;
    private Long id;
    private String reference;
    private User user;
    private Shop shop;

    public Order() {
    }

    public Order(String reference, User user, Shop shop) {
        this.id = ++counter;
        this.reference = reference;
        this.user = user;
        this.shop = shop;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}