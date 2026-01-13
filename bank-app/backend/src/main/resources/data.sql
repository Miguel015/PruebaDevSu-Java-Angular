-- Idempotent seeds: only insert if not exists
-- Persona
INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono)
SELECT 'Juan Perez', 'M', 35, '100200300', 'Calle Falsa 123', '3001112222'
WHERE NOT EXISTS (SELECT 1 FROM persona WHERE identificacion = '100200300');

-- Cliente (link to persona)
INSERT INTO cliente (persona_id, cliente_id, contrasena, estado)
SELECT p.id, 'CLI001', 'password', 1 FROM persona p
WHERE p.identificacion = '100200300' AND NOT EXISTS (SELECT 1 FROM cliente WHERE cliente_id = 'CLI001');

-- Cuentas
INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
SELECT '00010001', 'Ahorro', 1000.00, 1, c.persona_id FROM cliente c
WHERE c.cliente_id = 'CLI001' AND NOT EXISTS (SELECT 1 FROM cuenta WHERE numero_cuenta = '00010001');

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
SELECT '00010002', 'Corriente', 500.00, 1, c.persona_id FROM cliente c
WHERE c.cliente_id = 'CLI001' AND NOT EXISTS (SELECT 1 FROM cuenta WHERE numero_cuenta = '00010002');

-- Movimientos: reference cuentas via numero_cuenta
INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo, cuenta_id)
SELECT '2023-01-01 10:00:00', 'CREDITO', 1000.00, 1000.00, cu.id FROM cuenta cu
WHERE cu.numero_cuenta = '00010001' AND NOT EXISTS (SELECT 1 FROM movimiento mv WHERE mv.cuenta_id = cu.id AND mv.tipo_movimiento = 'CREDITO' AND mv.valor = 1000.00);

INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo, cuenta_id)
SELECT '2023-01-02 12:30:00', 'DEBITO', 200.00, 800.00, cu.id FROM cuenta cu
WHERE cu.numero_cuenta = '00010001' AND NOT EXISTS (SELECT 1 FROM movimiento mv WHERE mv.cuenta_id = cu.id AND mv.tipo_movimiento = 'DEBITO' AND mv.valor = 200.00);

-- Additional seed: second cliente (Maria) with accounts and movements
INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono)
SELECT 'Maria Lopez', 'F', 29, '200300400', 'Avenida Siempre Viva 456', '3003334444'
WHERE NOT EXISTS (SELECT 1 FROM persona WHERE identificacion = '200300400');

INSERT INTO cliente (persona_id, cliente_id, contrasena, estado)
SELECT p.id, 'CLI002', 'password', 1 FROM persona p
WHERE p.identificacion = '200300400' AND NOT EXISTS (SELECT 1 FROM cliente WHERE cliente_id = 'CLI002');

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
SELECT '00020001', 'Ahorro', 2500.00, 1, c.persona_id FROM cliente c
WHERE c.cliente_id = 'CLI002' AND NOT EXISTS (SELECT 1 FROM cuenta WHERE numero_cuenta = '00020001');

INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo_inicial, estado, cliente_id)
SELECT '00020002', 'Corriente', 1500.00, 1, c.persona_id FROM cliente c
WHERE c.cliente_id = 'CLI002' AND NOT EXISTS (SELECT 1 FROM cuenta WHERE numero_cuenta = '00020002');

INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo, cuenta_id)
SELECT '2023-02-10 09:15:00', 'CREDITO', 500.00, 2500.00, cu.id FROM cuenta cu
WHERE cu.numero_cuenta = '00020001' AND NOT EXISTS (SELECT 1 FROM movimiento mv WHERE mv.cuenta_id = cu.id AND mv.tipo_movimiento = 'CREDITO' AND mv.valor = 500.00);

INSERT INTO movimiento (fecha, tipo_movimiento, valor, saldo, cuenta_id)
SELECT '2023-02-11 14:45:00', 'DEBITO', 300.00, 2200.00, cu.id FROM cuenta cu
WHERE cu.numero_cuenta = '00020001' AND NOT EXISTS (SELECT 1 FROM movimiento mv WHERE mv.cuenta_id = cu.id AND mv.tipo_movimiento = 'DEBITO' AND mv.valor = 300.00);

-- Note: Using conditional inserts makes this script safe to run on each startup.
