package com.aurius;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ChallengeStore {

    private final Cache<String, String> cache;

    public ChallengeStore(@Value("${auth.challenge.ttl-seconds:15}") long ttlSeconds) {
        long ttlMillis = ttlSeconds * 1_000L;
        this.cache = Caffeine.newBuilder()
                .expireAfterWrite(ttlSeconds, TimeUnit.SECONDS)
                .maximumSize(10_000)
                .build();
    }

    public void put(String id, String rawPayload) {
        cache.put(id, rawPayload);
    }

    public Optional<String> consume(String id) {
        String payload = cache.getIfPresent(id);
        cache.asMap().remove(id);
        return Optional.ofNullable(payload);
    }
}
