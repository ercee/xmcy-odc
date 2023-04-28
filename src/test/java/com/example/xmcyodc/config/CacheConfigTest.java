package com.example.xmcyodc.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CacheConfigTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void testCacheManager() {
        assertNotNull(cacheManager.getCache("cryptoCurrencies"));
        assertNotNull(cacheManager.getCache("cryptoSymbols"));
    }

}
