package com.example.xmcyodc.controller;

import com.example.xmcyodc.dto.CryptoSummary;
import com.example.xmcyodc.service.CryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CryptoControllerTest {

    private CacheManager cacheManager;

    @Mock
    private CryptoService cryptoService;

    @InjectMocks
    private CryptoController cryptoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cacheManager = new ConcurrentMapCacheManager("cryptoCurrencies");
    }

    @Test
    public void testGetAllCryptos() {
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 31);
        Instant start = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant end = endDate.atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC);

        when(cryptoService.findAll(start, end)).thenReturn(new TreeSet<>());

        Set<CryptoSummary> result = cryptoController.getAllCryptos(startDate, endDate);

        assertNotNull(result);
    }

    @Test
    public void testGetCryptoInfo() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Instant startOfDay = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = date.atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC);
        String name = "Bitcoin";

        when(cryptoService.getCryptoInfo(name, startOfDay, endOfDay)).thenReturn(new CryptoSummary());

        CryptoSummary result = cryptoController.getCryptoInfo(name, date, date);

        assertNotNull(result);
    }

    @Test
    public void testGetHighestRangeCrypto() {
        LocalDate date = LocalDate.of(2022, 1, 1);
        Instant startOfDay = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = date.atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC);

        Set<CryptoSummary> cryptoSummaryTreeSet = new HashSet<>();
        CryptoSummary cryptoSummary = new CryptoSummary();
        cryptoSummary.setSymbol("BTC");
        cryptoSummaryTreeSet.add(cryptoSummary);
        when(cryptoService.findAll(eq(startOfDay), eq(endOfDay))).thenReturn(cryptoSummaryTreeSet);

        String result = cryptoController.getHighestRangeCrypto(date);

        assertEquals("BTC", result);
    }

}
