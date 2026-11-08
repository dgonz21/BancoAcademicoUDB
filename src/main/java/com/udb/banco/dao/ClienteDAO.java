package com.udb.banco.dao;

import com.udb.banco.conexion.ConexionBD;
import com.udb.banco.modelo.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void insertar(Cliente c) throws SQLException {
        String sql = """
            INSERT INTO clientes
            (codigo_cliente, dui, nombre_completo, telefono,
             correo, direccion, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCodigoCliente());
            ps.setString(2, c.getDui());
            ps.setString(3, c.getNombreCompleto());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getCorreo());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getEstado());
            ps.executeUpdate();
        }
    }

    public List<Cliente> listar() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY id DESC";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(new Cliente(
                    rs.getInt("id"),
                    rs.getString("codigo_cliente"),
                    rs.getString("dui"),
                    rs.getString("nombre_completo"),
                    rs.getString("telefono"),
                    rs.getString("correo"),
                    rs.getString("direccion"),
                    rs.getString("estado")
                ));
            }
        }
        return clientes;
    }

    public void actualizar(Cliente c) throws SQLException {
        String sql = """
            UPDATE clientes SET codigo_cliente=?, dui=?,
            nombre_completo=?, telefono=?, correo=?,
            direccion=?, estado=? WHERE id=?
            """;

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCodigoCliente());
            ps.setString(2, c.getDui());
            ps.setString(3, c.getNombreCompleto());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getCorreo());
            ps.setString(6, c.getDireccion());
            ps.setString(7, c.getEstado());
            ps.setInt(8, c.getId());
            ps.executeUpdate();
        }
    }

    public void desactivar(int id) throws SQLException {
        String sql = "UPDATE clientes SET estado='Inactivo' WHERE id=?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
