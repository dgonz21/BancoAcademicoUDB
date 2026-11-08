package com.udb.banco.dao;

import com.udb.banco.conexion.ConexionBD;
import com.udb.banco.modelo.CuentaBancaria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {

    public void insertar(CuentaBancaria cuenta) throws SQLException {
        String sql = """
            INSERT INTO cuentas_bancarias
            (numero_cuenta, cliente_id, tipo_cuenta, saldo, estado)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cuenta.getNumeroCuenta());
            ps.setInt(2, cuenta.getClienteId());
            ps.setString(3, cuenta.getTipoCuenta());
            ps.setBigDecimal(4, cuenta.getSaldo());
            ps.setString(5, cuenta.getEstado());
            ps.executeUpdate();
        }
    }

    public List<CuentaBancaria> listar() throws SQLException {
        List<CuentaBancaria> cuentas = new ArrayList<>();

        String sql = "SELECT * FROM cuentas_bancarias ORDER BY id DESC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cuentas.add(new CuentaBancaria(
                    rs.getInt("id"),
                    rs.getString("numero_cuenta"),
                    rs.getInt("cliente_id"),
                    rs.getString("tipo_cuenta"),
                    rs.getBigDecimal("saldo"),
                    rs.getString("estado")
                ));
            }
        }
        return cuentas;
    }
    public void desactivar(int id) throws SQLException {
        String sql = "UPDATE cuentas_bancarias SET estado='Inactiva' WHERE id=?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("Cuenta no encontrada");
            }
        }
    }
    public void actualizar(int id, String tipo, String estado) throws SQLException {
        String sql = "UPDATE cuentas_bancarias SET tipo_cuenta=?, estado=? WHERE id=?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, tipo);
            ps.setString(2, estado);
            ps.setInt(3, id);

            if (ps.executeUpdate() == 0) {
                throw new SQLException("Cuenta no encontrada");
            }
        }
    }
    public String obtenerNombreCliente(int clienteId) throws SQLException {
        String sql = "SELECT nombre_completo FROM clientes WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, clienteId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nombre_completo");
                }
            }
        }

        return "Cliente no encontrado";
    }
}