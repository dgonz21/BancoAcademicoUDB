package com.udb.banco.vista;

import com.udb.banco.dao.ClienteDAO;
import com.udb.banco.modelo.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private final ClienteDAO dao = new ClienteDAO();

    private final JTextField codigo = new JTextField();
    private final JTextField dui = new JTextField();
    private final JTextField nombre = new JTextField();
    private final JTextField telefono = new JTextField();
    private final JTextField correo = new JTextField();
    private final JTextField direccion = new JTextField();

    private final JComboBox<String> estado =
        new JComboBox<>(new String[]{"Activo", "Inactivo", "Bloqueado"});

    private final DefaultTableModel modelo = new DefaultTableModel(
        new String[]{"ID", "Codigo", "DUI", "Nombre", "Telefono",
                     "Correo", "Direccion", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };

    private final JTable tabla = new JTable(modelo);

    public VentanaPrincipal() {
        setTitle("Banco Academico UDB - Gestion de Clientes");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel formulario = new JPanel(new GridLayout(7, 2, 8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos del cliente"));

        agregarCampo(formulario, "Codigo:", codigo);
        agregarCampo(formulario, "DUI:", dui);
        agregarCampo(formulario, "Nombre completo:", nombre);
        agregarCampo(formulario, "Telefono:", telefono);
        agregarCampo(formulario, "Correo:", correo);
        agregarCampo(formulario, "Direccion:", direccion);

        formulario.add(new JLabel("Estado:"));
        formulario.add(estado);

        JPanel botones = new JPanel(new FlowLayout());

        JButton guardar = new JButton("Registrar");
        JButton actualizar = new JButton("Actualizar");
        JButton desactivar = new JButton("Desactivar");
        JButton limpiar = new JButton("Limpiar");
        JButton refrescar = new JButton("Consultar");

        botones.add(guardar);
        botones.add(actualizar);
        botones.add(desactivar);
        botones.add(limpiar);
        botones.add(refrescar);

        JPanel superior = new JPanel(new BorderLayout(8, 8));
        superior.add(formulario, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);

        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        guardar.addActionListener(e -> ejecutar(() -> {
            dao.insertar(obtenerCliente(0));
            cargarClientes();
            limpiarFormulario();
            mensaje("Cliente registrado correctamente");
        }));

        actualizar.addActionListener(e -> ejecutar(() -> {
            int id = obtenerIdSeleccionado();
            dao.actualizar(obtenerCliente(id));
            cargarClientes();
            limpiarFormulario();
            mensaje("Cliente actualizado");
        }));

        desactivar.addActionListener(e -> ejecutar(() -> {
            int id = obtenerIdSeleccionado();
            dao.desactivar(id);
            cargarClientes();
            limpiarFormulario();
            mensaje("Cliente desactivado");
        }));

        limpiar.addActionListener(e -> limpiarFormulario());

        refrescar.addActionListener(e -> ejecutar(this::cargarClientes));

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                int fila = tabla.convertRowIndexToModel(tabla.getSelectedRow());
                codigo.setText(valor(fila, 1));
                dui.setText(valor(fila, 2));
                nombre.setText(valor(fila, 3));
                telefono.setText(valor(fila, 4));
                correo.setText(valor(fila, 5));
                direccion.setText(valor(fila, 6));
                estado.setSelectedItem(valor(fila, 7));
            }
        });

        ejecutar(this::cargarClientes);
    }

    private void agregarCampo(JPanel panel, String etiqueta, JTextField campo) {
        panel.add(new JLabel(etiqueta));
        panel.add(campo);
    }

    private String valor(int fila, int columna) {
        return modelo.getValueAt(fila, columna).toString();
    }

    private Cliente obtenerCliente(int id) {
        return new Cliente(id, codigo.getText(), dui.getText(),
            nombre.getText(), telefono.getText(), correo.getText(),
            direccion.getText(), estado.getSelectedItem().toString());
    }

    private int obtenerIdSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            throw new IllegalArgumentException("Seleccione un cliente de la tabla");
        }
        return Integer.parseInt(
            tabla.getValueAt(fila, 0).toString()
        );
    }

    private void cargarClientes() throws Exception {
        modelo.setRowCount(0);
        for (Cliente c : dao.listar()) {
            modelo.addRow(new Object[]{
                c.getId(), c.getCodigoCliente(), c.getDui(),
                c.getNombreCompleto(), c.getTelefono(), c.getCorreo(),
                c.getDireccion(), c.getEstado()
            });
        }
    }

    private void limpiarFormulario() {
        codigo.setText("");
        dui.setText("");
        nombre.setText("");
        telefono.setText("");
        correo.setText("");
        direccion.setText("");
        estado.setSelectedIndex(0);
        tabla.clearSelection();
    }

    private void mensaje(String texto) {
        JOptionPane.showMessageDialog(this, texto);
    }

    private void ejecutar(Accion accion) {
        try {
            accion.ejecutar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @FunctionalInterface
    private interface Accion {
        void ejecutar() throws Exception;
    }
}