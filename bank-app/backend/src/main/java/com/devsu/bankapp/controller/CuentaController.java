package com.devsu.bankapp.controller;

import com.devsu.bankapp.dto.CuentaDTO;
import com.devsu.bankapp.service.CuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cuentas")
@Validated
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public ResponseEntity<CuentaDTO> create(@Valid @RequestBody CuentaDTO dto) { return ResponseEntity.ok(cuentaService.create(dto)); }

    @GetMapping
    public ResponseEntity<List<CuentaDTO>> all() { return ResponseEntity.ok(cuentaService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> get(@PathVariable Long id) { return ResponseEntity.ok(cuentaService.findById(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaDTO> update(@PathVariable Long id, @Valid @RequestBody CuentaDTO dto) { return ResponseEntity.ok(cuentaService.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { cuentaService.delete(id); return ResponseEntity.noContent().build(); }
}
