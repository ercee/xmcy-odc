package com.example.xmcyodc.controller;

import com.example.xmcyodc.dto.CryptoWrapper;
import com.example.xmcyodc.service.CryptoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.TreeSet;

@Tag(name = "CryptoController", description = "API for retrieving cryptocurrency information")
@RestController
@RequestMapping(path = "crypto")
public class CryptoController {

    @Autowired
    private CryptoService cryptoService;

    @Operation(summary = "Get all cryptocurrencies")
    @GetMapping("list")
    public TreeSet<CryptoWrapper> getAllCryptos() {
        TreeSet<CryptoWrapper> cryptos = cryptoService.findAll();
        return cryptos;
    }

    @Operation(summary = "Get cryptocurrency info by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the cryptocurrency"),
            @ApiResponse(responseCode = "404", description = "Cryptocurrency not found")
    })
    @GetMapping("{name}/info")
    public CryptoWrapper getCryptoInfo(@PathVariable String name) {
        CryptoWrapper crypto = cryptoService.getCryptoInfo(name);
        return crypto;
    }

    @Operation(summary = "Get highest range cryptocurrency by date")
    @GetMapping("highest-range/{date}")
    public String getHighestRangeCrypto(@PathVariable("date") LocalDate date) {
        Instant startOfDay = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = date.atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC);
        return cryptoService.findAll(startOfDay, endOfDay).first().getSymbol();
    }

}
