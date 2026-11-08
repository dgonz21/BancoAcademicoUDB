package com.udb.banco.controlador;

import com.udb.banco.dao.ClienteDAO;
import com.udb.banco.modelo.Cliente;
import java.sql.SQLException;
import java.util.List;

public class ClienteController {

    private final ClienteDAO dao;

    public ClienteController() {
        this.dao = new ClienteDAO();
    }

    public void registrar(Cliente cliente) throws SQLException {
        dao.insertar(cliente);
    }

    public List<Cliente> consultar() throws SQLException {
        return dao.listar();
    }

    public void actualizar(Cliente cliente) throws SQLException {
        dao.actualizar(cliente);
    }

    public void desactivar(int id) throws SQLException {
        dao.desactivar(id);
    }
}