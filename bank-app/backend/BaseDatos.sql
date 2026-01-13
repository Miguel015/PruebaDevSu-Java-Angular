-- Script SQL inicial - BaseDatos.sql

CREATE DATABASE IF NOT EXISTS banco_prueba;
USE banco_prueba;

CREATE TABLE persona (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  genero VARCHAR(20),
  edad INT,
  identificacion VARCHAR(50) UNIQUE,
  direccion VARCHAR(200),
  telefono VARCHAR(50)
);

CREATE TABLE cliente (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  persona_id BIGINT NOT NULL,
  cliente_id VARCHAR(100) UNIQUE NOT NULL,
  contrasena VARCHAR(255) NOT NULL,
  estado BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (persona_id) REFERENCES persona(id)
);

CREATE TABLE cuenta (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  numero_cuenta VARCHAR(50) UNIQUE NOT NULL,
  tipo_cuenta VARCHAR(20) NOT NULL,
  saldo_inicial DECIMAL(19,2) NOT NULL,
  estado BOOLEAN DEFAULT TRUE,
  cliente_id BIGINT NOT NULL,
  FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE movimiento (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  fecha DATETIME NOT NULL,
  tipo_movimiento VARCHAR(20) NOT NULL,
  valor DECIMAL(19,2) NOT NULL,
  saldo DECIMAL(19,2) NOT NULL,
  cuenta_id BIGINT NOT NULL,
  FOREIGN KEY (cuenta_id) REFERENCES cuenta(id)
);
