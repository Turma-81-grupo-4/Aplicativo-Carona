package com.generation.desafio_3_carona.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CaronaDTO {
    private Long id;
    private LocalDate dataViagem;
    private String origem;
    private String destino;
    private UsuarioDTO motorista;
    private int vagas;
    private double distancia;
    private double tempoViagem;

    public CaronaDTO() {}
    public CaronaDTO(Long id, LocalDate dataViagem, String origem, String destino, UsuarioDTO motorista) {
        this.id = id;
        this.dataViagem = dataViagem;
        this.origem = origem;
        this.destino = destino;
        this.motorista = motorista;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getTempoViagem() {
        return tempoViagem;
    }

    public void setTempoViagem(double tempoViagem) {
        this.tempoViagem = tempoViagem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataViagem() {
        return dataViagem;
    }

    public void setDataViagem(LocalDate dataViagem) {
        this.dataViagem = dataViagem;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public UsuarioDTO getMotorista() {
        return motorista;
    }

    public void setMotorista(UsuarioDTO motorista) {
        this.motorista = motorista;
    }

    public void setDataHoraPartida(LocalDateTime dataHoraPartida) {

    }
}
