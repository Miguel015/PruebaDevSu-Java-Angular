package com.devsu.bankapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ClienteDTO implements Serializable {
    private Long id;

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    // clienteId puede ser generado automáticamente en el backend cuando no se envía
    private String clienteId;

    @NotNull(message = "Estado es obligatorio")
    private Boolean estado;

    public ClienteDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
