package com.example.xmcyodc.repository;

import com.example.xmcyodc.model.CryptoCurrency;

import java.time.Instant;
import java.util.List;

public interface CryptoRepository {
    List<CryptoCurrency> getCryptos(String name, Instant start, Instant end);

    List<String> getCryptoSymbols();
}
