package br.com.fiap.bankapi.service;

import br.com.fiap.bankapi.dto.ContaRequest;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public String validateContaRequest(ContaRequest contaRequest) {
        if (contaRequest.getNomeTitular() == null || contaRequest.getNomeTitular().isEmpty()) {
            return "Nome do titular é obrigatório";
        }

        if (contaRequest.getCpfTitular() == null || contaRequest.getCpfTitular().isEmpty() || contaRequest.getCpfTitular().length() != 11) {
            return "CPF do titular é obrigatório e deve ter 11 dígitos";
        }

        if (contaRequest.getDataAbertura() == null || contaRequest.getDataAbertura().isEmpty()) {
            return "Data de abertura é obrigatória";
        }

        if (contaRequest.getSaldoInicial() < 0) {
            return "Saldo inicial não pode ser negativo";
        }

        if (contaRequest.getTipoConta() == null) {
            return "Tipo de conta deve ser válido (CORRENTE, POUPANCA, SALARIO)";
        }

        return null;
    }
}
