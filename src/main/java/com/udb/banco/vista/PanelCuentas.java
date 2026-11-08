package com.udb.banco.vista;

import com.udb.banco.controlador.CuentaController;
import com.udb.banco.controlador.ClienteController;
import com.udb.banco.modelo.CuentaBancaria;
import com.udb.banco.modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

public class PanelCuentas extends JPanel {

    private final CuentaController controller = new CuentaController();
    private final ClienteController clienteController = new ClienteController();

    private final JTextField numero = new JTextField();
        private final JTextField buscarCliente = new JTextField();
    private final JComboBox<ClienteItem> cliente = new JComboBox<>();
    private final JComboBox<String> tipo =
        new JComboBox<>(new String[]{"Ahorro", "Corriente"});
    private final JTextField saldo = new JTextField("0.00");

    private final DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"ID", "Numero de cuenta", "Propietario", "Tipo", "Saldo", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    public PanelCuentas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel formulario = new JPanel(new GridLayout(4, 2, 8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder("Nueva cuenta bancaria"));

        formulario.add(new JLabel("Numero de cuenta:"));
        formulario.add(numero);
        formulario.add(new JLabel("Cliente:"));
                JPanel panelBusqueda = new JPanel(new BorderLayout(5, 0));
        JButton botonBuscar = new JButton("Buscar");
        panelBusqueda.add(buscarCliente, BorderLayout.CENTER);
        panelBusqueda.add(botonBuscar, BorderLayout.EAST);
        formulario.add(panelBusqueda);
        formulario.add(new JLabel("Tipo de cuenta:"));
        formulario.add(tipo);
        formulario.add(new JLabel("Saldo inicial:"));
        formulario.add(saldo);

        JButton registrar = new JButton("Crear cuenta");
        JButton consultar = new JButton("Consultar cuentas");
        JButton eliminar = new JButton("Eliminar");
        JButton actualizar = new JButton("Actualizar cuenta");
        JButton actualizarClientes = new JButton("Actualizar clientes");

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(registrar);
        botones.add(consultar);
        botones.add(eliminar);
        botones.add(actualizar);
        botones.add(actualizarClientes);

        JPanel superior = new JPanel(new BorderLayout());
                JPanel campos = new JPanel(new BorderLayout(5, 5));
        campos.add(formulario, BorderLayout.NORTH);

        JPanel resultados = new JPanel(new BorderLayout(5, 5));
        resultados.add(new JLabel("Propietario seleccionado:"), BorderLayout.WEST);
        resultados.add(cliente, BorderLayout.CENTER);

        campos.add(resultados, BorderLayout.SOUTH);
        superior.add(campos, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        add(superior, BorderLayout.NORTH);
        JTable tablaCuentas = new JTable(modelo);
        tablaCuentas.removeColumn(tablaCuentas.getColumnModel().getColumn(0));
        add(new JScrollPane(tablaCuentas), BorderLayout.CENTER);

        registrar.addActionListener(e -> ejecutar(() -> {
            ClienteItem seleccionado = (ClienteItem) cliente.getSelectedItem();

            if (seleccionado == null) {
                throw new IllegalArgumentException("Seleccione un cliente");
            }

            CuentaBancaria cuenta = new CuentaBancaria(
                0,
                numero.getText(),
                seleccionado.id,
                tipo.getSelectedItem().toString(),
                new BigDecimal(saldo.getText().trim()),
                "Activa"
            );

            controller.registrar(cuenta);
            cargarCuentas();
            numero.setText("");
            saldo.setText("0.00");

            JOptionPane.showMessageDialog(this, "Cuenta creada correctamente");
        }));

                eliminar.addActionListener(e -> ejecutar(() -> {
            int fila = tablaCuentas.getSelectedRow();

            if (fila < 0) {
                throw new IllegalArgumentException(
                    "Seleccione una cuenta de la tabla"
                );
            }

            int id = Integer.parseInt(
                modelo.getValueAt(tablaCuentas.convertRowIndexToModel(fila), 0).toString()
            );

            int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar esta cuenta? Se marcara como Inactiva.",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                controller.desactivar(id);
                cargarCuentas();
                JOptionPane.showMessageDialog(
                    this, "Cuenta desactivada correctamente"
                );
            }
        }));

                actualizar.addActionListener(e -> ejecutar(() -> {
            int fila = tablaCuentas.getSelectedRow();

            if (fila < 0) {
                throw new IllegalArgumentException(
                    "Seleccione una cuenta de la tabla"
                );
            }

            int filaModelo = tablaCuentas.convertRowIndexToModel(fila);
            int id = Integer.parseInt(
                modelo.getValueAt(filaModelo, 0).toString()
            );

            String tipoActual = modelo.getValueAt(filaModelo, 3).toString();
            String estadoActual = modelo.getValueAt(filaModelo, 5).toString();

            JComboBox<String> tipos = new JComboBox<>(
                new String[]{"Ahorro", "Corriente"}
            );
            tipos.setSelectedItem(tipoActual);

            JComboBox<String> estados = new JComboBox<>(
                new String[]{"Activa", "Inactiva", "Bloqueada"}
            );
            estados.setSelectedItem(estadoActual);

            JPanel panel = new JPanel(new java.awt.GridLayout(2, 2, 8, 8));
            panel.add(new JLabel("Tipo:"));
            panel.add(tipos);
            panel.add(new JLabel("Estado:"));
            panel.add(estados);

            int respuesta = JOptionPane.showConfirmDialog(
                this, panel, "Actualizar cuenta",
                JOptionPane.OK_CANCEL_OPTION
            );

            if (respuesta == JOptionPane.OK_OPTION) {
                controller.actualizar(
                    id,
                    tipos.getSelectedItem().toString(),
                    estados.getSelectedItem().toString()
                );

                cargarCuentas();
                JOptionPane.showMessageDialog(
                    this, "Cuenta actualizada correctamente"
                );
            }
        }));

                botonBuscar.addActionListener(e -> ejecutar(this::cargarClientes));
        buscarCliente.addActionListener(e -> ejecutar(this::cargarClientes));

        consultar.addActionListener(e -> ejecutar(this::cargarCuentas));
        actualizarClientes.addActionListener(e -> ejecutar(this::cargarClientes));

        ejecutar(() -> {
            cargarClientes();
            cargarCuentas();
        });
    }

    private void cargarClientes() throws Exception {
        cliente.removeAllItems();

        String texto = buscarCliente.getText().trim();

        if (texto.isEmpty()) {
            return;
        }

        for (Cliente c : clienteController.buscarActivos(texto)) {
            cliente.addItem(new ClienteItem(
                c.getId(),
                c.getNombreCompleto() + " (" + c.getCodigoCliente() + ")"
            ));
        }

        if (cliente.getItemCount() == 0) {
            JOptionPane.showMessageDialog(
                this, "No se encontraron clientes activos"
            );
        }
    }
    private void cargarCuentas() throws Exception {
        modelo.setRowCount(0);

        for (CuentaBancaria c : controller.consultar()) {
            modelo.addRow(new Object[]{
                c.getId(),
                c.getNumeroCuenta(),
                controller.obtenerNombreCliente(c.getClienteId()),
                c.getTipoCuenta(),
                c.getSaldo(),
                c.getEstado()
            });
        }
    }

    private void ejecutar(Accion accion) {
        try {
            accion.ejecutar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @FunctionalInterface
    private interface Accion {
        void ejecutar() throws Exception;
    }

    private static class ClienteItem {
        final int id;
        final String nombre;

        ClienteItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }
}