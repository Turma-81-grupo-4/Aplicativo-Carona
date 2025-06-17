package com.generation.desafio_3_carona.dto;

import com.generation.desafio_3_carona.model.enums.StatusCarona;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CaronaUpdateDTO {
    private LocalDateTime dataHoraPartida;
    private String origem;
    private String destino;
    
    @NotNull
    private Integer distanciaKm;
    private Integer velocidade;
    private int vagas;
    private BigDecimal valorPorPassageiro;

    public LocalDateTime getDataHoraPartida() {
        return dataHoraPartida;
    }

    public void setDataHoraPartida(LocalDateTime dataHoraPartida) {
        this.dataHoraPartida = dataHoraPartida;
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

    public Integer getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Integer distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public Integer getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(Integer velocidade) {
        this.velocidade = velocidade;
    }

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public BigDecimal getValorPorPassageiro() {
        return valorPorPassageiro;
    }

    public void setValorPorPassageiro(BigDecimal valorPorPassageiro) {
        this.valorPorPassageiro = valorPorPassageiro;
    }

}