package br.com.fiap.bankapi.model;

public class Integrante {
    private String nome;
    private int rm;

    public Integrante(String nome, int rm) {
        this.nome = nome;
        this.rm = rm;
    }

    public String getNome() {
        return nome;
    }

    public int getRm() {
        return rm;
    }
}
