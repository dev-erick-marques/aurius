package com.aurius;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserStore {

    private final ConcurrentHashMap<String, String> store = new ConcurrentHashMap<>();

    public boolean register(String username, String publicKeyBase64) {
        return store.putIfAbsent(username, publicKeyBase64) == null;
    }

    public Optional<String> findPublicKey(String username) {
        return Optional.ofNullable(store.get(username));
    }
}
