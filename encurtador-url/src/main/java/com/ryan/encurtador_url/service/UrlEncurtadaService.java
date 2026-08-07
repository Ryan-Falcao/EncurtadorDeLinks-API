package com.ryan.encurtador_url.service;

import com.ryan.encurtador_url.entity.UrlEncurtada;
import com.ryan.encurtador_url.repository.UrlEncurtadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.server.UID;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlEncurtadaService {

    @Autowired
    private UrlEncurtadaRepository urlEncurtadaRepository;

    public UrlEncurtada encurtar(String urlOriginal){
        Optional<UrlEncurtada> existente = urlEncurtadaRepository.findByUrlOriginal(urlOriginal);
                if (existente.isPresent()){
                    return existente.get();
                }

                UrlEncurtada nova = new UrlEncurtada();
                nova.setUrlOriginal(urlOriginal);
                nova.setUrlEncurtada(gerarCodigo());
                nova.setDataDeCriacao(LocalDateTime.now());
                nova.setCliques(0);

                return urlEncurtadaRepository.save(nova);
    }
    private String gerarCodigo(){
        return UUID.randomUUID().toString().substring(0, 6);
    }
    public UrlEncurtada buscarPorCodigo(String codigo) {
        return urlEncurtadaRepository.findByUrlEncurtada(codigo)
                .orElseThrow(() -> new RuntimeException("URL não encontrada"));
    }

    public void incrementarCliques(UrlEncurtada url) {
        url.setCliques(url.getCliques() + 1);
        urlEncurtadaRepository.save(url);
    }
}
