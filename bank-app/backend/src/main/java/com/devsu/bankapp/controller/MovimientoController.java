package com.devsu.bankapp.controller;

import com.devsu.bankapp.dto.MovimientoDTO;
import com.devsu.bankapp.service.MovimientoService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
@Validated
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<MovimientoDTO> create(@Valid @RequestBody MovimientoDTO dto) {
        return ResponseEntity.ok(movimientoService.create(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDTO> update(@PathVariable Long id, @Valid @RequestBody MovimientoDTO dto) {
        return ResponseEntity.ok(movimientoService.update(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<MovimientoDTO>> all() { return ResponseEntity.ok(movimientoService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> get(@PathVariable Long id) { return ResponseEntity.ok(movimientoService.findById(id)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { movimientoService.delete(id); return ResponseEntity.noContent().build(); }
}
