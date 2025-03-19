package br.com.fiap.bankapi.controller;

import br.com.fiap.bankapi.model.Conta;
import br.com.fiap.bankapi.service.BankService;
import br.com.fiap.bankapi.service.ValidationService;
import br.com.fiap.bankapi.dto.ContaRequest;
import br.com.fiap.bankapi.dto.TransacaoRequest;
import br.com.fiap.bankapi.dto.PixRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BankController {

    private final BankService bankService;
    private final ValidationService validationService;

    public BankController(BankService bankService, ValidationService validationService) {
        this.bankService = bankService;
        this.validationService = validationService;
    }

    @PostMapping("/conta")
    public ResponseEntity<?> cadastrarConta(@RequestBody ContaRequest contaRequest) {
        String validationError = validationService.validateContaRequest(contaRequest);
        if (validationError != null) {
            return ResponseEntity.badRequest().body(validationError);
        }

        LocalDate dataAbertura = LocalDate.parse(contaRequest.getDataAbertura());
        Conta conta = bankService.cadastrarConta(contaRequest.getNumeroConta(), contaRequest.getAgencia(),
                contaRequest.getNomeTitular(), contaRequest.getCpfTitular(), dataAbertura, contaRequest.getSaldoInicial(),
                contaRequest.getTipoConta());
        return ResponseEntity.status(HttpStatus.CREATED).body(conta);
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

    @PutMapping("/conta/encerrar/{id}")
    public ResponseEntity<Conta> encerrarConta(@PathVariable Long id) {
        Conta conta = bankService.encerrarConta(id);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }

    @PutMapping("/conta/depositar")
    public ResponseEntity<?> realizarDeposito(@RequestBody TransacaoRequest transacaoRequest) {
        return realizarTransacao(transacaoRequest, "deposito");
    }

    @PutMapping("/conta/sacar")
    public ResponseEntity<?> realizarSaque(@RequestBody TransacaoRequest transacaoRequest) {
        return realizarTransacao(transacaoRequest, "saque");
    }

    @PostMapping("/conta/pix")
    public ResponseEntity<?> realizarPix(@RequestBody PixRequest pixRequest) {
        if (pixRequest.getValor() <= 0) {
            return ResponseEntity.badRequest().body("Valor recebido inválido");
        }

        Conta contaOrigem = bankService.realizarSaque(pixRequest.getIdContaOrigem(), pixRequest.getValor());
        if (contaOrigem == null) {
            return ResponseEntity.badRequest().body("Saldo insuficiente ou conta de origem não encontrada");
        }

        Conta contaDestino = bankService.realizarDeposito(pixRequest.getIdContaDestino(), pixRequest.getValor());
        if (contaDestino == null) {
            return ResponseEntity.badRequest().body("Conta de destino não encontrada");
        }

        return ResponseEntity.ok(contaOrigem);
    }

    private ResponseEntity<Conta> buscarConta(Long id, String cpf) {
        Conta conta = id != null ? bankService.buscarContaPorId(id) : bankService.buscarContaPorCpf(cpf);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> realizarTransacao(TransacaoRequest transacaoRequest, String tipoTransacao) {
        if (transacaoRequest.getValor() <= 0) {
            return ResponseEntity.badRequest().body("Valor recebido inválido");
        }

        Conta conta = tipoTransacao.equals("deposito") ?
                bankService.realizarDeposito(transacaoRequest.getId(), transacaoRequest.getValor()) :
                bankService.realizarSaque(transacaoRequest.getId(), transacaoRequest.getValor());

        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.badRequest().body("Saldo insuficiente ou conta não encontrada");
    }
}