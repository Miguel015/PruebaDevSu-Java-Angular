package com.devsu.bankapp.service.impl;

import com.devsu.bankapp.dto.ClienteDTO;
import com.devsu.bankapp.entity.Cliente;
import com.devsu.bankapp.exception.ResourceNotFoundException;
import com.devsu.bankapp.exception.BusinessException;
import com.devsu.bankapp.repository.ClienteRepository;
import com.devsu.bankapp.repository.CuentaRepository;
import com.devsu.bankapp.service.ClienteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository, CuentaRepository cuentaRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public ClienteDTO create(ClienteDTO dto) {
        // Generar clienteId si no viene desde la UI (timestamp para evitar duplicados)
        String clienteId = dto.getClienteId();
        if (clienteId == null || clienteId.trim().isEmpty()) {
            clienteId = "CLI" + System.currentTimeMillis();
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        // Usamos clienteId como identificacion mínima única en este ejercicio
        cliente.setIdentificacion(clienteId);
        cliente.setClienteId(clienteId);
        // Contraseña mínima requerida por el modelo; se deriva de clienteId para no exponerla en UI
        cliente.setContrasena(clienteId);
        cliente.setEstado(dto.getEstado() != null ? dto.getEstado() : true);
        clienteRepository.save(cliente);

        dto.setId(cliente.getId());
        dto.setClienteId(clienteId);
        return dto;
    }

    @Override
    public ClienteDTO update(Long id, ClienteDTO dto) {
        Cliente c = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        if (dto.getNombre() != null) c.setNombre(dto.getNombre());
        if (dto.getClienteId() != null) c.setClienteId(dto.getClienteId());
        if (dto.getEstado() != null) c.setEstado(dto.getEstado());
        clienteRepository.save(c);
        dto.setId(c.getId());
        return dto;
    }

    @Override
    public ClienteDTO findById(Long id) {
        Cliente c = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        ClienteDTO dto = new ClienteDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setClienteId(c.getClienteId());
        dto.setEstado(c.getEstado());
        return dto;
    }

    @Override
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream().map(c -> {
            ClienteDTO dto = new ClienteDTO();
            dto.setId(c.getId());
            dto.setNombre(c.getNombre());
            dto.setClienteId(c.getClienteId());
            dto.setEstado(c.getEstado());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Cliente c = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        if (!cuentaRepository.findByClienteId(id).isEmpty()) {
            throw new BusinessException("No se puede eliminar un cliente con cuentas asociadas");
        }
        clienteRepository.delete(c);
    }
}
