package com.example.xmcyodc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.time.Instant;

@JsonPropertyOrder(value = {"timestamp", "symbol", "price"})
public class CryptoCurrency {

    @JsonProperty("timestamp")
    private Instant timestamp;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("price")
    private BigDecimal price;

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
