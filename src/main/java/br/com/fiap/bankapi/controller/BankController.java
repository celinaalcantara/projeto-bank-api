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
@RequestMapping("/api")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping("/conta")
    public ResponseEntity<?> cadastrarConta(@RequestBody ContaRequest contaRequest) {
        try {
            if (contaRequest.getNomeTitular() == null || contaRequest.getNomeTitular().isEmpty()) {
                return ResponseEntity.badRequest().body("Nome do titular é obrigatório");
            }

            if (contaRequest.getCpfTitular() == null || contaRequest.getCpfTitular().isEmpty() || contaRequest.getCpfTitular().length() != 11) {
                return ResponseEntity.badRequest().body("CPF do titular é obrigatório e deve ter 11 dígitos");
            }

            LocalDate dataAbertura = LocalDate.parse(contaRequest.getDataAbertura());
            if (dataAbertura.isAfter(LocalDate.now())) {
                return ResponseEntity.badRequest().body("Data de abertura não pode ser no futuro");
            }

            if (contaRequest.getSaldoInicial() < 0) {
                return ResponseEntity.badRequest().body("Saldo inicial não pode ser negativo");
            }

            TipoConta tipoConta = contaRequest.getTipoConta();
            if (tipoConta == null) {
                return ResponseEntity.badRequest().body("Tipo de conta deve ser válido (CORRENTE, POUPANCA, SALARIO)");
            }

            Conta conta = bankService.cadastrarConta(contaRequest.getNumeroConta(), contaRequest.getAgencia(),
                    contaRequest.getNomeTitular(), contaRequest.getCpfTitular(), dataAbertura, contaRequest.getSaldoInicial(),
                    tipoConta);
            return ResponseEntity.status(HttpStatus.CREATED).body(conta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar a requisição: " + e.getMessage());
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

    @PutMapping("/conta/encerrar/{id}")
    public ResponseEntity<Conta> encerrarConta(@PathVariable Long id) {
        Conta conta = bankService.encerrarConta(id);
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }

    @PutMapping("/conta/depositar")
    public ResponseEntity<?> realizarDeposito(@RequestBody TransacaoRequest transacaoRequest) {
        if (transacaoRequest.getValor() <= 0) {
            return ResponseEntity.badRequest().body("Valor recebido inválido");
        }

        Conta conta = bankService.realizarDeposito(transacaoRequest.getId(), transacaoRequest.getValor());
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }

    @PutMapping("/conta/sacar")
    public ResponseEntity<?> realizarSaque(@RequestBody TransacaoRequest transacaoRequest) {
        if (transacaoRequest.getValor() <= 0) {
            return ResponseEntity.badRequest().body("Valor recebido inválido");
        }

        Conta conta = bankService.realizarSaque(transacaoRequest.getId(), transacaoRequest.getValor());
        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.badRequest().body("Saldo insuficiente ou conta não encontrada");
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
        Conta conta = null;
        if (id != null) {
            conta = bankService.buscarContaPorId(id);
        } else if (cpf != null) {
            conta = bankService.buscarContaPorCpf(cpf);
        }

        return conta != null ? ResponseEntity.ok(conta) : ResponseEntity.notFound().build();
    }

    public static class ContaRequest {
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

    public static class TransacaoRequest {
        private Long id;
        private Double valor;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Double getValor() {
            return valor;
        }

        public void setValor(Double valor) {
            this.valor = valor;
        }
    }

    public static class PixRequest {
        private Long idContaOrigem;
        private Long idContaDestino;
        private Double valor;

        public Long getIdContaOrigem() {
            return idContaOrigem;
        }

        public void setIdContaOrigem(Long idContaOrigem) {
            this.idContaOrigem = idContaOrigem;
        }

        public Long getIdContaDestino() {
            return idContaDestino;
        }

        public void setIdContaDestino(Long idContaDestino) {
            this.idContaDestino = idContaDestino;
        }

        public Double getValor() {
            return valor;
        }

        public void setValor(Double valor) {
            this.valor = valor;
        }
    }
}