package com.kenzie.appserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {


    @Bean
    public CacheStore StudyGroupCache() {
        int cacheDurationSeconds = 24; // 24 hours in seconds
        return new CacheStore(cacheDurationSeconds,TimeUnit.HOURS);
    }
}
