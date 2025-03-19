package br.com.fiap.bankapi.controller;

import br.com.fiap.bankapi.model.Conta;
import br.com.fiap.bankapi.model.TipoConta;
import br.com.fiap.bankapi.service.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/conta")
    public ResponseEntity<Conta> cadastrarConta(@RequestParam String numeroConta, @RequestParam String agencia,
                                                @RequestParam String nomeTitular, @RequestParam String cpfTitular,
                                                @RequestParam String dataAbertura, @RequestParam Double saldoInicial,
                                                @RequestParam TipoConta tipo) {
        try {
            LocalDate data = LocalDate.parse(dataAbertura);
            Conta conta = bankService.cadastrarConta(numeroConta, agencia, nomeTitular, cpfTitular, data, saldoInicial, tipo);
            return ResponseEntity.status(HttpStatus.CREATED).body(conta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/contas")
    public List<Conta> listarContas() {
        return bankService.listarContas();
    }

    @GetMapping("/conta/{id}")
    public ResponseEntity<Conta> buscarContaPorId(@PathVariable Long id) {
        return buscarConta(id, null);
    }

    @GetMapping("/conta/cpf/{cpf}")
    public ResponseEntity<Conta> buscarContaPorCpf(@PathVariable String cpf) {
        return buscarConta(null, cpf);
    }

    @PutMapping("/conta/{id}/encerrar")
    public ResponseEntity<Conta> encerrarConta(@PathVariable Long id) {
        Conta conta = bankService.encerrarConta(id);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }

    @PutMapping("/conta/{id}/depositar")
    public ResponseEntity<Conta> realizarDeposito(@PathVariable Long id, @RequestParam Double valor) {
        return realizarTransacao(id, valor, "deposito");
    }

    @PutMapping("/conta/{id}/sacar")
    public ResponseEntity<Conta> realizarSaque(@PathVariable Long id, @RequestParam Double valor) {
        return realizarTransacao(id, valor, "saque");
    }

    private ResponseEntity<Conta> buscarConta(Long id, String cpf) {
        Conta conta = null;
        if (id != null) {
            conta = bankService.buscarContaPorId(id);
        } else if (cpf != null) {
            conta = bankService.buscarContaPorCpf(cpf);
        }

        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }

    private ResponseEntity<Conta> realizarTransacao(Long id, Double valor, String tipoTransacao) {
        if (valor <= 0) {
            return ResponseEntity.badRequest().body(null);
        }

        Conta conta = tipoTransacao.equals("deposito") ?
                bankService.realizarDeposito(id, valor) : bankService.realizarSaque(id, valor);

        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }
}
