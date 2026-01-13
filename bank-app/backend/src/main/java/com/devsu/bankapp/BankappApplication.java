package com.devsu.bankapp;

import com.devsu.bankapp.entity.Cliente;
import com.devsu.bankapp.entity.Cuenta;
import com.devsu.bankapp.entity.Movimiento;
import com.devsu.bankapp.entity.Persona;
import com.devsu.bankapp.repository.ClienteRepository;
import com.devsu.bankapp.repository.CuentaRepository;
import com.devsu.bankapp.repository.MovimientoRepository;
import com.devsu.bankapp.repository.PersonaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootApplication
public class BankappApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankappApplication.class, args);
    }

    @Bean
    public CommandLineRunner seedData(PersonaRepository personaRepo, ClienteRepository clienteRepo,
                                      CuentaRepository cuentaRepo, MovimientoRepository movRepo) {
        return args -> {
            // If default client already exists, skip
            if (clienteRepo.findByClienteId("CLI001").isPresent()) return;

            Persona p = new Persona();
            p.setNombre("Juan Perez");
            p.setGenero("M");
            p.setEdad(35);
            p.setIdentificacion("100200300");
            p.setDireccion("Calle Falsa 123");
            p.setTelefono("3001112222");
            personaRepo.save(p);

            Cliente c = new Cliente();
            c.setId(p.getId());
            c.setNombre(p.getNombre());
            c.setGenero(p.getGenero());
            c.setEdad(p.getEdad());
            c.setIdentificacion(p.getIdentificacion());
            c.setDireccion(p.getDireccion());
            c.setTelefono(p.getTelefono());
            c.setClienteId("CLI001");
            c.setContrasena("password");
            c.setEstado(true);
            clienteRepo.save(c);

            Cuenta cu1 = new Cuenta();
            cu1.setNumeroCuenta("00010001");
            cu1.setTipoCuenta("Ahorro");
            cu1.setSaldoInicial(new BigDecimal("1000.00"));
            cu1.setEstado(true);
            cu1.setCliente(c);
            cuentaRepo.save(cu1);

            Cuenta cu2 = new Cuenta();
            cu2.setNumeroCuenta("00010002");
            cu2.setTipoCuenta("Corriente");
            cu2.setSaldoInicial(new BigDecimal("500.00"));
            cu2.setEstado(true);
            cu2.setCliente(c);
            cuentaRepo.save(cu2);

            Movimiento m1 = new Movimiento();
            m1.setFecha(LocalDateTime.now().minusDays(10));
            m1.setTipoMovimiento("CREDITO");
            m1.setValor(new BigDecimal("1000.00"));
            m1.setSaldo(new BigDecimal("1000.00"));
            m1.setCuenta(cu1);
            movRepo.save(m1);

            Movimiento m2 = new Movimiento();
            m2.setFecha(LocalDateTime.now().minusDays(5));
            m2.setTipoMovimiento("DEBITO");
            m2.setValor(new BigDecimal("200.00"));
            m2.setSaldo(new BigDecimal("800.00"));
            m2.setCuenta(cu1);
            movRepo.save(m2);
        };
    }
}
