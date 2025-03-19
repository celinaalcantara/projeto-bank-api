package br.com.fiap.bankapi.dto;

import br.com.fiap.bankapi.model.TipoConta;

public class ContaRequest {
    private String numeroConta;
    private String agencia;
    private String nomeTitular;
    private String cpfTitular;
    private String dataAbertura;
    private Double saldoInicial;
    private String tipo;

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

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public TipoConta getTipoConta() {
        try {
            return TipoConta.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
