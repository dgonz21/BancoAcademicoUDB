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
}