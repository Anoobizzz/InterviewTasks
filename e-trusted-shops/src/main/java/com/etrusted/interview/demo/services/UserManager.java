package com.etrusted.interview.demo.services;

import com.etrusted.interview.demo.entity.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class UserManager {
    private static MessageDigest digest;

    private Map<String, User> users = new Hashtable<>();

    @PostConstruct
    public void postConstruct() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-256");
    }

    public User saveUserOrGetExisting(String firstname, String lastname, String email) {
        String hashedEmail = hashEmail(email);
        User newUser = users.putIfAbsent(hashedEmail, new User(firstname, lastname, hashedEmail));
        return newUser == null ? users.get(hashedEmail) : newUser;
    }

    public User unsubscribe(String email) {
        return users.remove(hashEmail(email));
    }

    private String hashEmail(String email) {
        return DatatypeConverter.printHexBinary(digest.digest(email.getBytes(UTF_8)));
    }
}