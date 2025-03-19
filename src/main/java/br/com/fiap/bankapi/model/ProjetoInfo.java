package br.com.fiap.bankapi.model;

public class ProjetoInfo {
    private String projeto;
    private String nome;
    private int rm;

    public ProjetoInfo(String projeto, String nome, int rm) {
        this.projeto = projeto;
        this.nome = nome;
        this.rm = rm;
    }

    public String getProjeto() {
        return projeto;
    }

    public String getNome() {
        return nome;
    }

    public int getRm() {
        return rm;
    }
}
