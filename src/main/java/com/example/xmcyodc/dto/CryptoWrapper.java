package com.example.xmcyodc.dto;

import java.math.BigDecimal;

public class CryptoWrapper {
    private String symbol;
    private BigDecimal max;
    private BigDecimal min;
    private BigDecimal oldest;
    private BigDecimal newest;
    private BigDecimal normalizedRange;


    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public void setNormalizedRange(BigDecimal normalizedRange) {
        this.normalizedRange = normalizedRange;
    }

    public BigDecimal getNormalizedRange() {
        return normalizedRange;
    }

    public BigDecimal getOldest() {
        return oldest;
    }

    public void setOldest(BigDecimal oldest) {
        this.oldest = oldest;
    }

    public BigDecimal getNewest() {
        return newest;
    }

    public void setNewest(BigDecimal newest) {
        this.newest = newest;
    }


}
