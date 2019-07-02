package io.github.anoobizzz.mintostask.configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {
    public static final String WEATHER_API_CACHE = "weatherApiCache";
    public static final String GEOLOCATION_API_CACHE = "geolocationApiCache";

    @Bean
    public CacheManager cacheManager() {
        return new CaffeineCacheManager(WEATHER_API_CACHE, GEOLOCATION_API_CACHE);
    }


    @Bean
    public Cache weatherApiCache(@NonNull @Value("${weather.cache.eviction.time}") Long cacheEvictionTime) {
        return buildCache(WEATHER_API_CACHE, cacheEvictionTime);
    }

    @Bean
    public Cache geolocationApiCache(@NonNull @Value("${geolocation.cache.eviction.time}") Long cacheEvictionTime) {
        return buildCache(GEOLOCATION_API_CACHE, cacheEvictionTime);
    }

    private Cache buildCache(final String name, final Long cacheEvictionTime) {
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(cacheEvictionTime, TimeUnit.SECONDS)
                .maximumSize(100)
                .build());
    }
}
