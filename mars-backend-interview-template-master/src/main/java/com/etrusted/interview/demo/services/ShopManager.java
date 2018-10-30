package com.etrusted.interview.demo.services;

import com.etrusted.interview.demo.entity.Shop;
import org.springframework.stereotype.Component;

import java.util.Hashtable;
import java.util.Map;

@Component
public class ShopManager {
    private Map<String, Shop> shops = new Hashtable<>();

    public Shop saveShopOrGetExisting(String url) {
        Shop newShop = shops.putIfAbsent(url, new Shop(url));
        return newShop == null ? shops.get(url) : newShop;
    }
}