package com.example.xmcyodc.repository;

import com.example.xmcyodc.model.CryptoCurrency;
import com.example.xmcyodc.util.CsvUtility;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class CsvRepository implements CryptoRepository {
    private final CsvUtility csvUtility;

    public CsvRepository(CsvUtility csvUtility) {
        this.csvUtility = csvUtility;
    }

    @Cacheable(value = "cryptoCurrencies", key = "#name + '|' + #start + '|' + #end")
    @Override
    public List<CryptoCurrency> getCryptos(String name, Instant start, Instant end) {
        Stream<CryptoCurrency> stream;
        try {
            stream = csvUtility.readCsvFileOfCrypto(name).stream();
            if (start != null) {
                stream = stream.filter(e -> e.getTimestamp().isAfter(start));
            }
            if (end != null) {
                stream = stream.filter(e -> e.getTimestamp().isBefore(end));
            }
            return stream.toList();
        } catch (IOException e) {
            //TODO düzenle
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value = "cryptoSymbols")
    @Override
    public List<String> getCryptoSymbols() {
        try {
            return csvUtility.listCsvFiles().stream()
                    .filter(fileName -> fileName.matches("[A-Z]+_values\\.csv"))
                    .map(fileName -> fileName.substring(0, fileName.indexOf("_")))
                    .toList();
        } catch (IOException e) {
            //TODO manalı hata ekle
            throw new RuntimeException(e);
        }
    }
}
