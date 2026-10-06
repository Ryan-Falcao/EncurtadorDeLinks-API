package com.ryan.encurtador_url.service;

import com.ryan.encurtador_url.entity.UrlEncurtada;
import com.ryan.encurtador_url.repository.UrlEncurtadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UrlEncurtadaService {

    @Autowired
    private UrlEncurtadaRepository urlEncurtadaRepository;

    public UrlEncurtada encurtar(String urlOriginal){
        String urlValidada = validarUrl(urlOriginal);
        Optional<UrlEncurtada> existente = urlEncurtadaRepository.findByUrlOriginal(urlValidada);
                if (existente.isPresent()){
                    return existente.get();
                }

                UrlEncurtada nova = new UrlEncurtada();
                nova.setUrlOriginal(urlValidada);
                nova.setUrlEncurtada(gerarCodigo());
                nova.setDataDeCriacao(LocalDateTime.now());
                nova.setCliques(0);

                return urlEncurtadaRepository.save(nova);
    }

    private String validarUrl(String urlOriginal) {
        if (urlOriginal == null || urlOriginal.isBlank() || urlOriginal.length() > 2_048) {
            throw new IllegalArgumentException("Informe uma URL de até 2.048 caracteres.");
        }

        try {
            URI uri = new URI(urlOriginal.trim());
            if (!("http".equalsIgnoreCase(uri.getScheme()) || "https".equalsIgnoreCase(uri.getScheme()))
                    || uri.getHost() == null || uri.getUserInfo() != null) {
                throw new IllegalArgumentException("Informe uma URL HTTP ou HTTPS válida.");
            }
            return uri.toASCIIString();
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("Informe uma URL HTTP ou HTTPS válida.");
        }
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
