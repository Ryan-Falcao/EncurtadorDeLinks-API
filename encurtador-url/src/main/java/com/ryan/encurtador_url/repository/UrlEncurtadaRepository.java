package com.ryan.encurtador_url.repository;

import com.ryan.encurtador_url.entity.UrlEncurtada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlEncurtadaRepository extends JpaRepository<UrlEncurtada, Long> {

    Optional<UrlEncurtada> findByCodigoCurto(String codigoCurto);

    Optional<UrlEncurtada> findByUrlOriginal(String urlOriginal);
}
