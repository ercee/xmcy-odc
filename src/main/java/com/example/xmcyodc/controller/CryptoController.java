package com.example.xmcyodc.controller;

import com.example.xmcyodc.dto.CryptoSummary;
import com.example.xmcyodc.service.CryptoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Set;

@Tag(name = "CryptoController", description = "API for retrieving cryptocurrency information")
@RestController
@RequestMapping(path = "crypto")
public class CryptoController {

    @Autowired
    private CryptoService cryptoService;

    @Operation(summary = "Get all cryptocurrencies")
    @GetMapping("list")
    public Set<CryptoSummary> getAllCryptos(@RequestParam(name = "startDate", required = false) LocalDate startDate,
                                            @RequestParam(name = "endDate", required = false) LocalDate endDate) {
        Instant start = null;
        if (startDate != null) {
            start = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        }
        Instant end = null;
        if (endDate != null) {
            end = endDate.atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC);
        }
        return cryptoService.findAll(start, end);
    }

    @Operation(summary = "Get cryptocurrency info by name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the cryptocurrency"),
            @ApiResponse(responseCode = "404", description = "Cryptocurrency not found")
    })
    @GetMapping("{name}/info")
    public CryptoSummary getCryptoInfo(@PathVariable String name,
                                       @RequestParam(name = "startDate", required = false) LocalDate startDate,
                                       @RequestParam(name = "endDate", required = false) LocalDate endDate) {
        Instant start = null;
        if (startDate != null) {
            start = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        }
        Instant end = null;
        if (endDate != null) {
            end = endDate.atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC);
        }

        return cryptoService.getCryptoInfo(name, start, end);
    }

    @Operation(summary = "Get highest range cryptocurrency by date")
    @GetMapping("highest-range/{date}")
    public String getHighestRangeCrypto(@PathVariable("date") LocalDate date) {
        Instant startOfDay = date.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant endOfDay = date.atStartOfDay().plusDays(1).toInstant(ZoneOffset.UTC);
        return cryptoService.findAll(startOfDay, endOfDay).iterator().next().getSymbol();
    }

}
