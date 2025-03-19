package br.com.fiap.bankapi.model;

import java.util.List;

public class ProjetoInfo {
    private String projeto;
    private List<Integrante> integrantes;

    public ProjetoInfo(String projeto, List<Integrante> integrantes) {
        this.projeto = projeto;
        this.integrantes = integrantes;
    }

    public String getProjeto() {
        return projeto;
    }

    public List<Integrante> getIntegrantes() {
        return integrantes;
    }
}
