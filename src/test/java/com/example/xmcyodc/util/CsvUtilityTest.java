package com.example.xmcyodc.util;

import com.example.xmcyodc.model.CryptoCurrency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class CsvUtilityTest {


    @Autowired
    private CsvUtility csvUtility;

    @Test
    void readCsvFile() throws IOException {
        List<CryptoCurrency> cryptoCurrencies = csvUtility.readCsvFileOfCrypto("BTC");
        assertNotNull(cryptoCurrencies, "The cryptoCurrencies list should not be null");
        assertEquals(100, cryptoCurrencies.size(), "The cryptoCurrencies list should contain 100 elements");

        // Assert the values of the first CryptoCurrency object in the list
        CryptoCurrency cryptoCurrency = cryptoCurrencies.get(0);
        assertEquals(1641009600000L, cryptoCurrency.getTimestamp().toEpochMilli(),
                "The timestamp of the first CryptoCurrency object should be 1641009600000");
        assertEquals("46813.21", cryptoCurrency.getPrice().toString(),
                "The price of the first CryptoCurrency object should be 46813.21");
        assertEquals("BTC", cryptoCurrency.getSymbol(),
                "The symbol of the first CryptoCurrency object should be 'BTC'");
    }


    @Test
    public void testListCsvFiles() throws IOException {
        List<String> csvFiles = csvUtility.listCsvFiles();
        assertEquals(2, csvFiles.size(), "Expected size of the CSV file list is not correct.");
        assertEquals("BTC_values.csv", csvFiles.get(0), "The name of the CSV file in the list is not correct.");
        assertEquals("DOGE_values.csv", csvFiles.get(1), "The name of the CSV file in the list is not correct.");
    }
}