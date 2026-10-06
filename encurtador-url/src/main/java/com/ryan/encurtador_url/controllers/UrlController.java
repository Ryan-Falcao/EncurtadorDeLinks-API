package com.ryan.encurtador_url.controllers;


import com.ryan.encurtador_url.entity.UrlEncurtada;
import com.ryan.encurtador_url.request.CriarUrlRequest;
import com.ryan.encurtador_url.service.RateLimitService;
import com.ryan.encurtador_url.service.UrlEncurtadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("/api/urls")
public class UrlController {

    @Autowired
    private UrlEncurtadaService urlEncurtadaService;

    @Autowired
    private RateLimitService rateLimitService;

    @PostMapping
    public ResponseEntity<?> encurtar(@RequestBody(required = false) CriarUrlRequest request,
                                      HttpServletRequest httpRequest){
        RateLimitService.Resultado limite = rateLimitService.verificar(ipDoVisitante(httpRequest));
        if (!limite.permitido()) {
            return ResponseEntity.status(429)
                    .header("Retry-After", String.valueOf(limite.segundosRestantes()))
                    .body(Map.of("mensagem", "Muitas tentativas. Aguarde antes de criar outro link."));
        }

        try {
            UrlEncurtada url = urlEncurtadaService.encurtar(request == null ? null : request.getUrlOriginal());
            return ResponseEntity.ok(url);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(Map.of("mensagem", exception.getMessage()));
        }
    }

    private String ipDoVisitante(HttpServletRequest request) {
        String encaminhado = request.getHeader("X-Forwarded-For");
        if (encaminhado != null && !encaminhado.isBlank()) {
            return encaminhado.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
