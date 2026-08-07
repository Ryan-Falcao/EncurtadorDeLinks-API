package com.ryan.encurtador_url.controllers;


import com.ryan.encurtador_url.entity.UrlEncurtada;
import com.ryan.encurtador_url.request.CriarUrlRequest;
import com.ryan.encurtador_url.service.UrlEncurtadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/urls")
public class UrlController {

    @Autowired
    private UrlEncurtadaService urlEncurtadaService;

    @PostMapping
    public ResponseEntity<UrlEncurtada> encurtar(@RequestBody CriarUrlRequest request){
        UrlEncurtada url = urlEncurtadaService.encurtar(request.getUrlOriginal());
        return ResponseEntity.ok(url);
    }
}
