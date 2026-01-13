package com.devsu.bankapp.controller;

import com.devsu.bankapp.dto.ReportDTO;
import com.devsu.bankapp.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/reportes")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<ReportDTO> getReport(@RequestParam Long clienteId,
                                               @RequestParam String fechaInicio,
                                               @RequestParam String fechaFin) {
        LocalDateTime start = parseToStartOfDay(fechaInicio);
        LocalDateTime end = parseToEndOfDay(fechaFin);
        ReportDTO report = reportService.generateReport(clienteId, start, end);
        return ResponseEntity.ok(report);
    }

    private LocalDateTime parseToStartOfDay(String input) {
        if (input == null || input.isBlank()) return LocalDateTime.MIN;
        try {
            if (input.length() == 10) { // yyyy-MM-dd
                return java.time.LocalDate.parse(input).atStartOfDay();
            }
            return LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception ex) {
            // fallback: try parsing with common patterns
            try {
                return LocalDateTime.parse(input);
            } catch (Exception e) {
                throw new IllegalArgumentException("fechaInicio inválida: " + input, e);
            }
        }
    }

    private LocalDateTime parseToEndOfDay(String input) {
        if (input == null || input.isBlank()) return LocalDateTime.MAX;
        try {
            if (input.length() == 10) { // yyyy-MM-dd
                return java.time.LocalDate.parse(input).atTime(23, 59, 59);
            }
            return LocalDateTime.parse(input, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception ex) {
            try {
                return LocalDateTime.parse(input);
            } catch (Exception e) {
                throw new IllegalArgumentException("fechaFin inválida: " + input, e);
            }
        }
    }
}
