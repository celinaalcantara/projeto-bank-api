package br.com.fiap.bankapi.controller;

import br.com.fiap.bankapi.model.ProjetoInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class InfoController {

    @GetMapping("/")
    public ResponseEntity<ProjetoInfo> getProjetoInfo() {

        ProjetoInfo projetoInfo = new ProjetoInfo("BankAPI", "Celina Alcantara do Carmo", 558090);

        return ResponseEntity.ok(projetoInfo);
    }
}
