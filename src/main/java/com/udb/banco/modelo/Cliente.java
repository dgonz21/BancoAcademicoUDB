package com.udb.banco.modelo;

public class Cliente {
    private int id;
    private String codigoCliente;
    private String dui;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String direccion;
    private String estado;

    public Cliente(int id, String codigoCliente, String dui,
                   String nombreCompleto, String telefono,
                   String correo, String direccion, String estado) {
        this.id = id;
        this.codigoCliente = validarTexto(codigoCliente, "Codigo");
        this.dui = validarTexto(dui, "DUI");
        this.nombreCompleto = validarTexto(nombreCompleto, "Nombre");
        this.telefono = validarTexto(telefono, "Telefono");
        this.correo = validarTexto(correo, "Correo");
        this.direccion = validarTexto(direccion, "Direccion");
        setEstado(estado);
    }

    private String validarTexto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
        return valor.trim();
    }

    public int getId() { return id; }
    public String getCodigoCliente() { return codigoCliente; }
    public String getDui() { return dui; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public String getEstado() { return estado; }

    public void setEstado(String estado) {
        if (!"Activo".equals(estado) &&
            !"Inactivo".equals(estado) &&
            !"Bloqueado".equals(estado)) {
            throw new IllegalArgumentException("Estado invalido");
        }
        this.estado = estado;
    }
}
