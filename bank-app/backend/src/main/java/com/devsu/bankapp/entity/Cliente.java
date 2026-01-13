package com.devsu.bankapp.entity;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "persona_id")
public class Cliente extends Persona {

    @NotBlank
    @Size(max = 100)
    @Column(name = "cliente_id", unique = true, nullable = false)
    private String clienteId;

    @NotBlank
    @Size(min = 6)
    private String contrasena;

    private Boolean estado = true;

    public Cliente() { super(); }

    public String getClienteId() { return clienteId; }
    public void setClienteId(String clienteId) { this.clienteId = clienteId; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }
}
