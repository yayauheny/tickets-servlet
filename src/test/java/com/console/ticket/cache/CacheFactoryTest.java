package com.console.ticket.cache;

import com.console.ticket.exception.FileException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheFactoryTest {
    CacheFactory instance;

    @BeforeEach
    void initialize(){
        instance = new CacheFactory();
    }
    @Test
    void getCache() {
        assertThrows(FileException.class, ()-> instance.getCache());
    }
}