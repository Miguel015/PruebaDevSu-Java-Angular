package com.devsu.bankapp.service;

import com.devsu.bankapp.dto.ClienteDTO;
import com.devsu.bankapp.entity.Cliente;
import com.devsu.bankapp.repository.ClienteRepository;
import com.devsu.bankapp.service.impl.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void crearCliente_llamaRepositorios() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Prueba");
        dto.setClienteId("cli123");
        dto.setEstado(true);

        Cliente clienteSaved = new Cliente();
        clienteSaved.setId(10L);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSaved);

        clienteService.create(dto);
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    public void updateCliente_whenNotFound_throws() {
        ClienteDTO dto = new ClienteDTO();
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());
        try {
            clienteService.update(999L, dto);
            throw new AssertionError("Expected exception");
        } catch (RuntimeException ex) {
            // correcto
        }
    }
}
