package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.service.model.StudyGroup;

import java.util.concurrent.TimeUnit;

public class CacheStore {
    // we are storing it in local memory
    // constricts the size of memory and type of key-value pairs
    private Cache<String, StudyGroup> cache;

    public CacheStore(int expiry, TimeUnit timeUnit) {
        // initialize the cache
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public StudyGroup get(String key) {
        // Retrieve and return the studyGroup
        return cache.getIfPresent(key);
    }

    public void evict(String key) {
        // Invalidate/evict the StudyGroup from cache
        if(key != null){
            cache.invalidate(key);
        }
    }

    public void add(String key, StudyGroup value) {
        // Add studyGroup to cache
        cache.put(key,value);
    }
}
