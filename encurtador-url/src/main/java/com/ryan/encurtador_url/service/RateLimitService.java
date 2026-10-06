package com.ryan.encurtador_url.service;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private static final int LIMITE_POR_JANELA = 10;
    private static final long JANELA_EM_MILLIS = Duration.ofHours(1).toMillis();
    private static final int MAXIMO_DE_IPS_MONITORADOS = 10_000;
    private final ConcurrentHashMap<String, Janela> tentativasPorIp = new ConcurrentHashMap<>();

    public synchronized Resultado verificar(String ip) {
        long agora = System.currentTimeMillis();
        if (tentativasPorIp.size() >= MAXIMO_DE_IPS_MONITORADOS) {
            tentativasPorIp.clear();
        }

        Janela janela = tentativasPorIp.computeIfAbsent(ip, chave -> new Janela(agora));
        if (agora - janela.inicio > JANELA_EM_MILLIS) {
            janela.inicio = agora;
            janela.quantidade = 0;
        }

        if (janela.quantidade >= LIMITE_POR_JANELA) {
            long segundosRestantes = Math.max(1, (JANELA_EM_MILLIS - (agora - janela.inicio)) / 1_000);
            return new Resultado(false, segundosRestantes);
        }

        janela.quantidade++;
        return new Resultado(true, 0);
    }

    private static class Janela {
        private long inicio;
        private int quantidade;

        private Janela(long inicio) {
            this.inicio = inicio;
        }
    }

    public record Resultado(boolean permitido, long segundosRestantes) {}
}
