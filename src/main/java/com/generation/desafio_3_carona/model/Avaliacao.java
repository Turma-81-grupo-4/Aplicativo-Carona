package com.generation.desafio_3_carona.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_avaliacoes")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avaliador_id")
    private Usuario avaliador;

    @ManyToOne
    @JoinColumn(name = "avaliado_id")
    private Usuario avaliado;

    @ManyToOne
    @JoinColumn(name = "carona_id")
    private Carona carona;

    @Min(1)
    @Max(5)
    private int nota; // Nota de 1 a 5

    @Column(columnDefinition = "TEXT")
    private String comentario;

    private LocalDateTime dataAvaliacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getAvaliador() {
        return avaliador;
    }

    public void setAvaliador(Usuario avaliador) {
        this.avaliador = avaliador;
    }

    public Usuario getAvaliado() {
        return avaliado;
    }

    public void setAvaliado(Usuario avaliado) {
        this.avaliado = avaliado;
    }

    public Carona getCarona() {
        return carona;
    }

    public void setCarona(Carona carona) {
        this.carona = carona;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDateTime dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }
}