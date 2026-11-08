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
    private final JComboBox<ClienteItem> cliente = new JComboBox<>();
    private final JComboBox<String> tipo =
        new JComboBox<>(new String[]{"Ahorro", "Corriente"});
    private final JTextField saldo = new JTextField("0.00");

    private final DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"ID", "Numero", "Cliente ID", "Tipo", "Saldo", "Estado"}, 0) {
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
        formulario.add(cliente);
        formulario.add(new JLabel("Tipo de cuenta:"));
        formulario.add(tipo);
        formulario.add(new JLabel("Saldo inicial:"));
        formulario.add(saldo);

        JButton registrar = new JButton("Crear cuenta");
        JButton consultar = new JButton("Consultar cuentas");
        JButton eliminar = new JButton("Eliminar");
        JButton actualizarClientes = new JButton("Actualizar clientes");

        JPanel botones = new JPanel(new FlowLayout());
        botones.add(registrar);
        botones.add(consultar);
        botones.add(eliminar);
        botones.add(actualizarClientes);

        JPanel superior = new JPanel(new BorderLayout());
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        add(superior, BorderLayout.NORTH);
        JTable tablaCuentas = new JTable(modelo);
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
                tablaCuentas.getValueAt(fila, 0).toString()
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

        consultar.addActionListener(e -> ejecutar(this::cargarCuentas));
        actualizarClientes.addActionListener(e -> ejecutar(this::cargarClientes));

        ejecutar(() -> {
            cargarClientes();
            cargarCuentas();
        });
    }

    private void cargarClientes() throws Exception {
        cliente.removeAllItems();

        for (Cliente c : clienteController.consultar()) {
            if ("Activo".equals(c.getEstado())) {
                cliente.addItem(new ClienteItem(c.getId(), c.getNombreCompleto()));
            }
        }
    }

    private void cargarCuentas() throws Exception {
        modelo.setRowCount(0);

        for (CuentaBancaria c : controller.consultar()) {
            modelo.addRow(new Object[]{
                c.getId(),
                c.getNumeroCuenta(),
                c.getClienteId(),
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
            return id + " - " + nombre;
        }
    }
}