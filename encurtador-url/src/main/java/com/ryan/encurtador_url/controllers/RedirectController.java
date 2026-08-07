package com.ryan.encurtador_url.controllers;

import com.ryan.encurtador_url.entity.UrlEncurtada;
import com.ryan.encurtador_url.service.UrlEncurtadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;

@Controller
public class RedirectController {

    @Autowired private UrlEncurtadaService urlEncurtadaService;

    @GetMapping("/{codigo}")
    public ResponseEntity<Void> redirecionar(@PathVariable String codigo){
        UrlEncurtada url = urlEncurtadaService.buscarPorCodigo(codigo);
        urlEncurtadaService.incrementarCliques(url);

        return ResponseEntity.status(302)
                .location(URI.create(url.getUrlOriginal()))
                .build();
    }
}
