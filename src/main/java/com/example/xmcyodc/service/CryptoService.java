package com.example.xmcyodc.service;

import com.example.xmcyodc.dto.CryptoSummary;
import com.example.xmcyodc.model.CryptoCurrency;
import com.example.xmcyodc.repository.CryptoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class CryptoService {
    private final CryptoRepository cryptoRepository;

    public CryptoService(CryptoRepository cryptoRepository) {
        this.cryptoRepository = cryptoRepository;
    }

    /**
     * Returns a descending sorted set of CryptoWrapper objects, where each CryptoWrapper represents a group of
     * CryptoCurrency objects with the same symbol.
     * The CryptoWrapper objects are sorted based on their normalized range (i.e. (max - min) / min).
     *
     * @return A descending sorted set of CryptoWrapper objects.
     */
    public TreeSet<CryptoSummary> findAll(Instant start, Instant end) {
        List<String> cryptoNames = cryptoRepository.getCryptoSymbols();
        return cryptoNames.stream().map(e -> getCryptoInfo(e, start, end))
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(CryptoSummary::getNormalizedRange)
                        .reversed())));
    }

    public TreeSet<CryptoSummary> findAll() {
        return findAll(null, null);
    }


    public CryptoSummary getCryptoInfo(String name, Instant start, Instant end) {
        List<CryptoCurrency> cryptoList = cryptoRepository.getCryptos(name, start, end);
        CryptoSummary cryptoSummary = new CryptoSummary();
        cryptoSummary.setSymbol(name);
        BigDecimal max = cryptoList.stream().map(CryptoCurrency::getPrice).max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        cryptoSummary.setMax(max);
        BigDecimal min = cryptoList.stream().map(CryptoCurrency::getPrice).min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        cryptoSummary.setMin(min);
        BigDecimal normalizedRange = max.subtract(min).divide(min, RoundingMode.HALF_UP);
        cryptoSummary.setNormalizedRange(normalizedRange);
        BigDecimal newestPrice = cryptoList.stream()
                .max(Comparator.comparing(CryptoCurrency::getTimestamp))
                .map(CryptoCurrency::getPrice).orElse(BigDecimal.ZERO);
        cryptoSummary.setNewest(newestPrice);
        BigDecimal oldestPrice = cryptoList.stream()
                .min(Comparator.comparing(CryptoCurrency::getTimestamp))
                .map(CryptoCurrency::getPrice).orElse(BigDecimal.ZERO);
        cryptoSummary.setOldest(oldestPrice);
        return cryptoSummary;
    }

}
