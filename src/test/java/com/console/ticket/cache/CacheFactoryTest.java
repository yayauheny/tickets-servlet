package com.console.ticket.cache;

import com.console.ticket.service.cache.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class CacheFactoryTest {
    CacheFactory instance;

    @BeforeEach
    void initialize() {
        instance = new CacheFactory();
    }

    @DisplayName("assert that getCache() return LRU cache")
    @Test
    void checkReturnLruCache() {
        Cache cacheList = instance.getCache(3, CacheType.LRU);

        assertThat(cacheList).isInstanceOf(LruCache.class);
    }

    @DisplayName("assert that getCache() return LFU cache as default")
    @Test
    void checkReturnLfuCacheAsDefault() {
        Cache cacheList = instance.getCache(3, CacheType.LFU);

        assertThat(cacheList).isInstanceOf(LfuCache.class);
    }
}