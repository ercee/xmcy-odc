package com.example.xmcyodc.model;

import java.math.BigDecimal;
import java.time.Instant;

public class CryptoCurrency {

    private Instant timestamp;
    private String symbol;
    private BigDecimal price;

    public CryptoCurrency() {
    }

    public CryptoCurrency(Instant timestamp, String symbol, BigDecimal price) {
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.price = price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
