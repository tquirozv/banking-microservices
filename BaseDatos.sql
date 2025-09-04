-- BaseDatos.sql
-- Database schema for Banking Microservices

-- psql -h <HOST> -U <ADMIN_USER> -f BaseDatos.sql

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_catalog.pg_roles WHERE rolname = 'testuser') THEN
        CREATE ROLE testuser WITH LOGIN PASSWORD 'pass';
    END IF;
END
$$;

-- Create database owned by testuser
CREATE DATABASE banking_db OWNER testuser;

-- Connect to the database
\c banking_db

-- Switch to testuser for table creation
SET ROLE testuser;

-- Person table (base class)
CREATE TABLE personas (
    identificacion VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    genero VARCHAR(10),
    edad INTEGER,
    direccion VARCHAR(500),
    telefono VARCHAR(15),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Client table (inherits from Person)
CREATE TABLE clientes (
    clienteid BIGSERIAL PRIMARY KEY,
    persona_id VARCHAR(20) NOT NULL REFERENCES personas(identificacion) ON DELETE CASCADE,
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Account table
CREATE TABLE cuentas (
    numero_cuenta VARCHAR(20) PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    tipo_cuenta VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    saldo_actual DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    estado BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Movements table
CREATE TABLE movimientos (
    id BIGSERIAL PRIMARY KEY,
    cuenta_id VARCHAR(20) NOT NULL REFERENCES cuentas(numero_cuenta) ON DELETE CASCADE,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(20) NOT NULL CHECK (tipo_movimiento IN ('CREDITO', 'DEBITO')),
    valor DECIMAL(15,2) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL,
    descripcion VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for better performance
CREATE INDEX IF NOT EXISTS idx_movimientos_cuenta_fecha ON movimientos(cuenta_id, fecha DESC);
CREATE INDEX IF NOT EXISTS idx_movimientos_tipo ON movimientos(tipo_movimiento);
CREATE INDEX IF NOT EXISTS idx_clientes_estado ON clientes(estado);
CREATE INDEX IF NOT EXISTS idx_clientes_persona_id ON clientes(persona_id);



-- Sample data
INSERT INTO personas (nombre, genero, edad, identificacion, direccion, telefono) VALUES 
('Jose Lema', 'M', 20, '2345678', 'Calle 122 #45-67', '555-0001'),
('Marianela Montalvo', 'F', 30, '3456789', 'Calle 121 #45-67', '555-0002'),
('Juan Osorio', 'M', 30, '4567890', 'Calle 120 #45-67', '555-0003'),
('Juan Perez', 'M', 30, '1234567', 'Calle 123 #45-67', '555-0004'),
('Maria Garcia', 'F', 25, '87654321', 'Carrera 89 #12-34', '555-0005'),
('Carlos Rodriguez', 'M', 35, '11223344', 'Avenida 56 #78-90', '555-0006');

INSERT INTO clientes (persona_id, contrasena, estado) VALUES 
('2345678', '1234', true),
('3456789', '5678', true),
('4567890', '9012', true),
('1234567', '12345', true),
('87654321', '56789', false),
('11223344', '90123', true);

INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_actual, cliente_id) VALUES 
('478758', 'AHORRO', 2000.00, 2000.00, 1),
('225487', 'CORRIENTE', 100.00, 100.00, 2),
('495878', 'AHORRO', 0.00, 0.00, 3),
('496825', 'AHORRO', 540.00, 540.00, 2),
('585545', 'CORRIENTE', 1000.00, 1000.00, 1),
('001-000001', 'AHORRO', 1000.00, 1000.00, 4),
('001-000002', 'CORRIENTE', 500.00, 500.00, 5),
('001-000003', 'AHORRO', 2000.00, 2000.00, 6);

INSERT INTO movimientos (cuenta_id, tipo_movimiento, valor, saldo, descripcion) VALUES 
('478758', 'CREDITO', 2000.00, 2000.00, 'Depósito inicial'),
('225487', 'CREDITO', 100.00, 100.00, 'Depósito inicial'),
('495878', 'CREDITO', 0.00, 0.00, 'Depósito inicial'),
('496825', 'CREDITO', 540.00, 540.00, 'Depósito inicial'),
('585545', 'CREDITO', 1000.00, 1000.00, 'Depósito inicial'),
('001-000001', 'CREDITO', 1000.00, 1000.00, 'Depósito inicial'),
('001-000002', 'CREDITO', 500.00, 500.00, 'Depósito inicial'),
('001-000003', 'CREDITO', 2000.00, 2000.00, 'Depósito inicial'),
('478758', 'DEBITO', 575.00, 1425.00, 'Retiro de 575'),
('225487', 'CREDITO', 100.00, 600.00, 'Depósito de 100'),
('495878', 'CREDITO', 150.00, 150.00, 'Depósito de 150'),
('496825', 'DEBITO', 540.00, 0.00, 'Retiro de 540');


-- Grant privileges to testuser
GRANT ALL PRIVILEGES ON DATABASE banking_db TO testuser;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO testuser;

GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO testuser;

GRANT USAGE ON SCHEMA public TO testuser;

ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO testuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO testuser;