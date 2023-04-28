package com.example.xmcyodc.repository;

import com.example.xmcyodc.model.CryptoCurrency;
import com.example.xmcyodc.util.CsvUtility;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CsvRepositoryTest {

    @Mock
    private CsvUtility csvUtility;

    @InjectMocks
    private CsvRepository csvRepository;

    public CsvRepositoryTest() {
    }

    @Test
    public void testGetCryptos() throws IOException {
        // Given
        String name = "Bitcoin";
        Instant start = Instant.parse("2022-01-01T00:00:00Z");
        Instant end = Instant.parse("2022-01-31T23:59:59Z");
        List<CryptoCurrency> expected = Arrays.asList(
                new CryptoCurrency(Instant.parse("2022-01-02T12:00:00Z"), "BTC", new BigDecimal("50000.0")),
                new CryptoCurrency(Instant.parse("2022-01-15T16:30:00Z"), "BTC", new BigDecimal("55000.0")),
                new CryptoCurrency(Instant.parse("2022-01-31T08:45:00Z"), "BTC", new BigDecimal("60000.0"))
        );
        when(csvUtility.readCsvFileOfCrypto(name)).thenReturn(expected);

        // When9
        List<CryptoCurrency> actual = csvRepository.getCryptos(name, start, end);

        // Then
        assertThat(actual).isEqualTo(expected.stream()
                .filter(e -> e.getTimestamp().isAfter(start))
                .filter(e -> e.getTimestamp().isBefore(end))
                .collect(Collectors.toList()));
        verify(csvUtility).readCsvFileOfCrypto(name);
    }


    @Test
    public void testGetCryptoSymbols() throws IOException {
        // arrange
        when(csvUtility.listCsvFiles()).thenReturn(Arrays.asList(
                "BTC_values.csv",
                "ETH_values.csv",
                "ADA_values.csv",
                "DOGE_values.csv",
                "XRP_values.csv"
        ));

        CsvRepository repository = new CsvRepository(csvUtility);

        // act
        List<String> result = repository.getCryptoSymbols();

        // assert
        assertEquals(5, result.size());
        assertTrue(result.contains("BTC"));
        assertTrue(result.contains("ETH"));
        assertTrue(result.contains("ADA"));
        assertTrue(result.contains("DOGE"));
        assertTrue(result.contains("XRP"));
    }


}
