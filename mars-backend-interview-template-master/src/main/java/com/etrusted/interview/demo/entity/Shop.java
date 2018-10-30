package com.etrusted.interview.demo.entity;

public class Shop {
    private static Long counter = 0L;
    private Long id;
    private String url;

    public Shop() {
    }

    public Shop(String url) {
        this.id = ++counter;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}