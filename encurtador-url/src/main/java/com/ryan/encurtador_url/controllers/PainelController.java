package com.ryan.encurtador_url.controllers;

import com.ryan.encurtador_url.entity.UrlEncurtada;
import com.ryan.encurtador_url.repository.UrlEncurtadaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/api/painel")
public class PainelController {

    private final UrlEncurtadaRepository urlEncurtadaRepository;
    private final String senhaAdmin;

    public PainelController(UrlEncurtadaRepository urlEncurtadaRepository,
                            @Value("${app.admin-password:}") String senhaAdmin) {
        this.urlEncurtadaRepository = urlEncurtadaRepository;
        this.senhaAdmin = senhaAdmin;
    }

    @GetMapping("/visao-geral")
    public ResponseEntity<?> visaoGeral(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorization) {
        if (senhaAdmin.isBlank()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErroPainel("O painel ainda não foi configurado no servidor."));
        }

        if (!autorizado(authorization)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Linkly\"")
                    .body(new ErroPainel("Senha inválida."));
        }

        List<LinkResumo> links = urlEncurtadaRepository.findTop100ByOrderByDataDeCriacaoDesc().stream()
                .map(this::resumir)
                .toList();
        long totalCliques = urlEncurtadaRepository.totalDeCliques();

        return ResponseEntity.ok(new VisaoGeral(urlEncurtadaRepository.count(), totalCliques, links));
    }

    private boolean autorizado(String authorization) {
        if (authorization == null || !authorization.startsWith("Basic ")) return false;

        String esperado = "admin:" + senhaAdmin;
        String recebido = authorization.substring(6);
        String esperadoCodificado = Base64.getEncoder()
                .encodeToString(esperado.getBytes(StandardCharsets.UTF_8));
        return esperadoCodificado.equals(recebido);
    }

    private LinkResumo resumir(UrlEncurtada url) {
        return new LinkResumo(url.getUrlEncurtada(), url.getUrlOriginal(), url.getCliques(), url.getDataDeCriacao());
    }

    public record VisaoGeral(long linksCriados, long totalCliques, List<LinkResumo> links) {}
    public record LinkResumo(String codigo, String urlOriginal, Integer cliques, LocalDateTime criadoEm) {}
    public record ErroPainel(String mensagem) {}
}
