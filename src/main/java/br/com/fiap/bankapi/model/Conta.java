package br.com.fiap.bankapi.model;

import java.time.LocalDate;

public class Conta {

    private Long id;
    private String numeroConta;
    private String agencia;
    private String nomeTitular;
    private String cpfTitular;
    private LocalDate dataAbertura;
    private Double saldo;
    private boolean ativa;
    private TipoConta tipo;

    public Conta(Long id, String numeroConta, String agencia, String nomeTitular, String cpfTitular,
                 LocalDate dataAbertura, Double saldo, boolean ativa, TipoConta tipo) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.nomeTitular = nomeTitular;
        this.cpfTitular = cpfTitular;
        this.dataAbertura = dataAbertura;
        this.saldo = saldo;
        this.ativa = ativa;
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

    public String getCpfTitular() {
        return cpfTitular;
    }

    public void setCpfTitular(String cpfTitular) {
        this.cpfTitular = cpfTitular;
    }

    public LocalDate getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDate dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public boolean ativa() {
        return ativa;
    }

    public TipoConta getTipo() {
        return tipo;
    }

    public void setTipo(TipoConta tipo) {
        this.tipo = tipo;
    }
}
