package br.com.fiap.bankapi.controller;

import br.com.fiap.bankapi.model.ProjetoInfo;
import br.com.fiap.bankapi.model.Integrante;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/")
public class InfoController {

    @GetMapping("/")
    public ResponseEntity<ProjetoInfo> getProjetoInfo() {
        Integrante integrante = new Integrante("Celina Alcantara do Carmo", 558090);
        ProjetoInfo projetoInfo = new ProjetoInfo("BankAPI", Collections.singletonList(integrante));
        return ResponseEntity.ok(projetoInfo);
    }
}
