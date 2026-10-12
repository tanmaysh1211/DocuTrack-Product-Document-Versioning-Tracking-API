package com.tanmaysharma.designledger.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

final class SafeCacheErrorHandler implements CacheErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(SafeCacheErrorHandler.class);

    @Override public void handleCacheGetError(RuntimeException ex, Cache cache, Object key) { warn(ex, cache, key); }
    @Override public void handleCachePutError(RuntimeException ex, Cache cache, Object key, Object value) { warn(ex, cache, key); }
    @Override public void handleCacheEvictError(RuntimeException ex, Cache cache, Object key) { warn(ex, cache, key); }
    @Override public void handleCacheClearError(RuntimeException ex, Cache cache) { warn(ex, cache, "all"); }

    private void warn(RuntimeException ex, Cache cache, Object key) {
        log.warn("Redis cache operation failed; continuing with the database. cache={}, key={}",
                cache.getName(), key, ex);
    }
}
