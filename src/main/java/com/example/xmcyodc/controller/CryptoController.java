package com.example.xmcyodc.controller;

import com.example.xmcyodc.dto.CryptoWrapper;
import com.example.xmcyodc.service.CryptoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.TreeSet;

@RestController
@RequestMapping(path = "crypto")
public class CryptoController {
    private final CryptoService cryptoService;

    public CryptoController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @GetMapping("list")
    public TreeSet<CryptoWrapper> getAllCryptos() {
        TreeSet<CryptoWrapper> cryptos = cryptoService.findAll();
        return cryptos;
    }

    @GetMapping("{name}/info")
    public CryptoWrapper getCryptoInfo(@PathVariable String name) {
        CryptoWrapper crypto = cryptoService.getCryptoInfo(name);
        return crypto;
    }

    @GetMapping("crypto/highest-range/{date}")
    public String getHighestRangeCrypto(@PathVariable("date") LocalDate date) {
        Instant startOfDay = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = date.atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC);
        return cryptoService.findAll(startOfDay, endOfDay).first().getSymbol();
    }

}
