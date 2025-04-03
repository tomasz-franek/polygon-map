package org.polygonMap.backend.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
@Log4j2
public class CacheConfiguration {
    
    @Bean
    CacheManager cacheManager() {
        log.info("Register cache manager");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.registerCustomCache("slideShows", createDefaultCache());
        return cacheManager;
    }

    private Cache<Object, Object> createDefaultCache() {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        cacheBuilder.initialCapacity(10);
        cacheBuilder.expireAfterWrite(Duration.ofMillis(15));
        cacheBuilder.expireAfterAccess(Duration.ofMinutes(15));
        cacheBuilder.maximumSize(200);
        return cacheBuilder.build();
    }
}
