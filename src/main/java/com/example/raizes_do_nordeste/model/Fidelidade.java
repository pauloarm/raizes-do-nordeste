package com.example.raizes_do_nordeste.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fidelidade")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fidelidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int saldoPontos;

    @Column(nullable = false)
    private LocalDateTime dataUltimaAtualizacao;

    @OneToOne
    @JoinColumn(name = "usuaruio_id", nullable = false)
    private Usuario cliente;

}
