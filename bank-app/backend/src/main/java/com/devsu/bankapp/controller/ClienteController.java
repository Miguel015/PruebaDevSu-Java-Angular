package com.devsu.bankapp.controller;

import com.devsu.bankapp.dto.ClienteDTO;
import com.devsu.bankapp.service.ClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clientes")
@Validated
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteDTO dto) {
        return ResponseEntity.ok(clienteService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> all() { return ResponseEntity.ok(clienteService.findAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> get(@PathVariable Long id) { return ResponseEntity.ok(clienteService.findById(id)); }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) { return ResponseEntity.ok(clienteService.update(id, dto)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { clienteService.delete(id); return ResponseEntity.noContent().build(); }
}
