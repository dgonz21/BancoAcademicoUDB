package com.udb.banco.modelo;

import java.math.BigDecimal;

public class CuentaBancaria {
    private int id;
    private String numeroCuenta;
    private int clienteId;
    private String tipoCuenta;
    private BigDecimal saldo;
    private String estado;

    public CuentaBancaria(int id, String numeroCuenta, int clienteId,
                          String tipoCuenta, BigDecimal saldo, String estado) {
        if (numeroCuenta == null || numeroCuenta.isBlank())
            throw new IllegalArgumentException("Numero de cuenta obligatorio");

        if (clienteId <= 0)
            throw new IllegalArgumentException("Cliente invalido");

        if (!"Ahorro".equals(tipoCuenta) && !"Corriente".equals(tipoCuenta))
            throw new IllegalArgumentException("Tipo de cuenta invalido");

        if (saldo == null || saldo.signum() < 0)
            throw new IllegalArgumentException("Saldo invalido");

        if (!"Activa".equals(estado) &&
            !"Inactiva".equals(estado) &&
            !"Bloqueada".equals(estado))
            throw new IllegalArgumentException("Estado invalido");

        this.id = id;
        this.numeroCuenta = numeroCuenta.trim();
        this.clienteId = clienteId;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
        this.estado = estado;
    }

    public int getId() { return id; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public int getClienteId() { return clienteId; }
    public String getTipoCuenta() { return tipoCuenta; }
    public BigDecimal getSaldo() { return saldo; }
    public String getEstado() { return estado; }
}