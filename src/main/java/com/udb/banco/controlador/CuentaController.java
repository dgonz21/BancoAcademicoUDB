package com.udb.banco.controlador;

import com.udb.banco.dao.CuentaDAO;
import com.udb.banco.modelo.CuentaBancaria;
import java.sql.SQLException;
import java.util.List;

public class CuentaController {

    private final CuentaDAO dao = new CuentaDAO();

    public void registrar(CuentaBancaria cuenta) throws SQLException {
        dao.insertar(cuenta);
    }

    public List<CuentaBancaria> consultar() throws SQLException {
        return dao.listar();
    }
    public void desactivar(int id) throws SQLException {
        dao.desactivar(id);
    }
    public void actualizar(int id, String tipo, String estado) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Cuenta invalida");
        }

        if (!"Ahorro".equals(tipo) && !"Corriente".equals(tipo)) {
            throw new IllegalArgumentException("Tipo de cuenta invalido");
        }

        if (!"Activa".equals(estado) &&
            !"Inactiva".equals(estado) &&
            !"Bloqueada".equals(estado)) {
            throw new IllegalArgumentException("Estado invalido");
        }

        dao.actualizar(id, tipo, estado);
    }
    public String obtenerNombreCliente(int clienteId) throws SQLException {
        return dao.obtenerNombreCliente(clienteId);
    }
}