package br.com.fiap.bankapi.service;

import br.com.fiap.bankapi.model.Conta;
import br.com.fiap.bankapi.model.TipoConta;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BankService {

    private static final Map<Long, Conta> contas = new HashMap<>();
    private static Long contador = 1L;

    public Conta cadastrarConta(String numeroConta, String agencia, String nomeTitular, String cpfTitular,
                                LocalDate dataAbertura, Double saldoInicial, TipoConta tipo) {

        if (nomeTitular == null || nomeTitular.isEmpty()) {
            throw new IllegalArgumentException("Nome do titular é obrigatório");
        }

        if (cpfTitular == null || cpfTitular.isEmpty()) {
            throw new IllegalArgumentException("CPF do titular é obrigatório");
        }

        if (dataAbertura.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de abertura não pode ser no futuro");
        }

        if (saldoInicial < 0) {
            throw new IllegalArgumentException("Saldo inicial não pode ser negativo");
        }

        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de conta deve ser válido");
        }

        Conta conta = new Conta(contador++, numeroConta, agencia, nomeTitular, cpfTitular, dataAbertura, saldoInicial, true, tipo);
        contas.put(conta.getId(), conta);
        return conta;
    }

    public List<Conta> listarContas() {
        return new ArrayList<>(contas.values());
    }

    public Conta buscarContaPorId(Long id) {
        return contas.get(id);
    }

    public Conta buscarContaPorCpf(String cpfTitular) {
        return contas.values().stream()
                .filter(conta -> conta.getCpfTitular().equals(cpfTitular))
                .findFirst()
                .orElse(null);
    }

    public Conta encerrarConta(Long id) {
        Conta conta = contas.get(id);
        if (conta != null) {
            conta.setAtiva(false);
        }
        return conta;
    }

    public Conta realizarDeposito(Long id, Double valor) {
        Conta conta = contas.get(id);
        if (conta != null && valor > 0) {
            conta.setSaldo(conta.getSaldo() + valor);
        }
        return conta;
    }

    public Conta realizarSaque(Long id, Double valor) {
        Conta conta = contas.get(id);
        if (conta != null && valor > 0 && conta.getSaldo() >= valor) {
            conta.setSaldo(conta.getSaldo() - valor);
        }
        return conta;
    }
}
