package com.devsu.bankapp.service;

import com.devsu.bankapp.dto.ReportDTO;

import java.time.LocalDateTime;

public interface ReportService {
    ReportDTO generateReport(Long clienteId, LocalDateTime inicio, LocalDateTime fin);
}
