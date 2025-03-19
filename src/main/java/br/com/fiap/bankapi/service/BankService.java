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
            return conta;
        }
        return null;
    }

    public Conta realizarSaque(Long id, Double valor) {
        Conta conta = contas.get(id);
        if (conta != null && valor > 0 && conta.getSaldo() >= valor) {
            conta.setSaldo(conta.getSaldo() - valor);
            return conta;
        }
        return null;
    }
}
