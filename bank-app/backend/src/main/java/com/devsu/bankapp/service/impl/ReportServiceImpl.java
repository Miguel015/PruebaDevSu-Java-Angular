package com.devsu.bankapp.service.impl;

import com.devsu.bankapp.dto.ClienteDTO;
import com.devsu.bankapp.dto.CuentaDTO;
import com.devsu.bankapp.dto.MovimientoDTO;
import com.devsu.bankapp.dto.ReportDTO;
import com.devsu.bankapp.entity.Cliente;
import com.devsu.bankapp.entity.Cuenta;
import com.devsu.bankapp.entity.Movimiento;
import com.devsu.bankapp.exception.ResourceNotFoundException;
import com.devsu.bankapp.repository.ClienteRepository;
import com.devsu.bankapp.repository.CuentaRepository;
import com.devsu.bankapp.repository.MovimientoRepository;
import com.devsu.bankapp.service.ReportService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public ReportServiceImpl(ClienteRepository clienteRepository, CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public ReportDTO generateReport(Long clienteId, LocalDateTime inicio, LocalDateTime fin) {
        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(cliente.getId());
        clienteDTO.setNombre(cliente.getNombre());
        clienteDTO.setClienteId(cliente.getClienteId());
        clienteDTO.setEstado(cliente.getEstado());

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        List<CuentaDTO> cuentasDto = cuentas.stream().map(c -> {
            CuentaDTO dto = new CuentaDTO();
            dto.setId(c.getId());
            dto.setNumeroCuenta(c.getNumeroCuenta());
            dto.setTipoCuenta(c.getTipoCuenta());
            dto.setSaldoInicial(c.getSaldoInicial());
            dto.setEstado(c.getEstado());
            dto.setClienteId(c.getCliente() != null ? c.getCliente().getId() : null);
            return dto;
        }).collect(Collectors.toList());

        BigDecimal totalDebitos = BigDecimal.ZERO;
        BigDecimal totalCreditos = BigDecimal.ZERO;
        Map<String, BigDecimal> saldos = new HashMap<>();

        for (Cuenta c : cuentas) {
            List<Movimiento> movimientos = movimientoRepository.findByCuentaIdAndFechaBetween(c.getId(), inicio, fin);
            BigDecimal debitos = movimientos.stream()
                    .filter(m -> m.getValor().compareTo(BigDecimal.ZERO) < 0)
                    .map(m -> m.getValor().abs())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal creditos = movimientos.stream()
                    .filter(m -> m.getValor().compareTo(BigDecimal.ZERO) > 0)
                    .map(Movimiento::getValor)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalDebitos = totalDebitos.add(debitos);
            totalCreditos = totalCreditos.add(creditos);
            saldos.put(c.getNumeroCuenta(), c.getSaldoInicial() != null ? c.getSaldoInicial() : BigDecimal.ZERO);
        }

        ReportDTO report = new ReportDTO();
        report.setCliente(clienteDTO);
        report.setCuentas(cuentasDto);
        report.setTotalDebitos(totalDebitos);
        report.setTotalCreditos(totalCreditos);
        report.setSaldos(saldos);

        // generar PDF simple
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);
            PDPageContentStream content = new PDPageContentStream(doc, page);
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 14);
            content.newLineAtOffset(50, 750);
            content.showText("Reporte de Movimientos - Cliente: " + (cliente.getNombre() != null ? cliente.getNombre() : ""));
            content.newLineAtOffset(0, -20);
            content.setFont(PDType1Font.HELVETICA, 12);
            content.showText("Periodo: " + inicio.toLocalDate() + " - " + fin.toLocalDate());
            content.newLineAtOffset(0, -20);
            content.showText("Total Creditos: " + totalCreditos);
            content.newLineAtOffset(0, -20);
            content.showText("Total Debitos: " + totalDebitos);
            content.newLineAtOffset(0, -20);
            content.showText("Saldos:");
            for (Map.Entry<String, BigDecimal> e : saldos.entrySet()) {
                content.newLineAtOffset(0, -15);
                content.showText(e.getKey() + ": " + e.getValue());
            }
            content.endText();
            content.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.save(baos);
            String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
            report.setPdfBase64(base64);
        } catch (Exception ex) {
            // no detallar al cliente; dejar PDF null
            report.setPdfBase64(null);
        }

        return report;
    }
}
