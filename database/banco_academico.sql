CREATE DATABASE IF NOT EXISTS banco_academico
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE banco_academico;

CREATE TABLE IF NOT EXISTS clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_cliente VARCHAR(20) NOT NULL UNIQUE,
    dui VARCHAR(10) NOT NULL UNIQUE,
    nombre_completo VARCHAR(120) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(120) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    estado ENUM('Activo','Inactivo','Bloqueado')
        NOT NULL DEFAULT 'Activo'
);

CREATE TABLE IF NOT EXISTS cuentas_bancarias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    cliente_id INT NOT NULL,
    tipo_cuenta ENUM('Ahorro','Corriente')
        NOT NULL DEFAULT 'Ahorro',
    saldo DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    estado ENUM('Activa','Inactiva','Bloqueada')
        NOT NULL DEFAULT 'Activa',
    fecha_apertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cuenta_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT chk_saldo_no_negativo CHECK (saldo >= 0)
);
