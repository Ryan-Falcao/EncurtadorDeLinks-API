package com.ryan.encurtador_url.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "UrlsEncurtadas")
@Getter
@Setter
public class UrlEncurtada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String urlOriginal;

    private String urlEncurtada;

    private LocalDateTime dataDeCriacao;

    private Integer cliques = 0;
}
