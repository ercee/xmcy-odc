package com.example.xmcyodc.service;

import com.example.xmcyodc.dto.CryptoSummary;
import com.example.xmcyodc.model.CryptoCurrency;
import com.example.xmcyodc.repository.CryptoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CryptoServiceTest {
    @Mock
    private CryptoRepository cryptoRepository;

    private CryptoService cryptoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cryptoService = new CryptoService(cryptoRepository);
    }

    @Test
    void testGetCryptoInfo() {
        String symbol = "BTC";
        Instant start = Instant.parse("2022-01-01T00:00:00Z");
        Instant end = Instant.parse("2022-01-07T23:59:59Z");

        CryptoCurrency crypto1 = new CryptoCurrency();
        crypto1.setPrice(new BigDecimal("5000"));
        crypto1.setTimestamp(Instant.parse("2022-01-01T12:00:00Z"));
        crypto1.setSymbol("BTC");

        CryptoCurrency crypto2 = new CryptoCurrency();
        crypto2.setPrice(new BigDecimal("55000"));
        crypto2.setTimestamp(Instant.parse("2022-01-02T12:00:00Z"));
        crypto2.setSymbol("BTC");

        CryptoCurrency crypto3 = new CryptoCurrency();
        crypto3.setPrice(new BigDecimal("60000"));
        crypto3.setTimestamp(Instant.parse("2022-01-03T12:00:00Z"));
        crypto3.setSymbol("BTC");

        List<CryptoCurrency> cryptoList = Arrays.asList(crypto1, crypto2, crypto3);
        when(cryptoRepository.getCryptoSymbols()).thenReturn(Arrays.asList(symbol));
        when(cryptoRepository.getCryptos(eq(symbol), eq(start), eq(end))).thenReturn(cryptoList);

        CryptoSummary cryptoSummary = cryptoService.getCryptoInfo(symbol, start, end);

        assertEquals(symbol, cryptoSummary.getSymbol());
        assertEquals(new BigDecimal("5000"), cryptoSummary.getMin());
        assertEquals(new BigDecimal("60000"), cryptoSummary.getMax());
        assertEquals(new BigDecimal("11"), cryptoSummary.getNormalizedRange());
        assertEquals(new BigDecimal("60000"), cryptoSummary.getNewest());
        assertEquals(new BigDecimal("5000"), cryptoSummary.getOldest());
    }
}
