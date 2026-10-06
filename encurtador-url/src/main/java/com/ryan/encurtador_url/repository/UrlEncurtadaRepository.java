package com.ryan.encurtador_url.repository;

import com.ryan.encurtador_url.entity.UrlEncurtada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UrlEncurtadaRepository extends JpaRepository<UrlEncurtada, Long> {

    Optional<UrlEncurtada> findByUrlEncurtada(String urlEncurtada);

    Optional<UrlEncurtada> findByUrlOriginal(String urlOriginal);

    List<UrlEncurtada> findTop100ByOrderByDataDeCriacaoDesc();

    @Query("select coalesce(sum(u.cliques), 0) from UrlEncurtada u")
    Long totalDeCliques();
}
