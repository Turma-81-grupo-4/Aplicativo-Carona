package com.generation.desafio_3_carona.dto;

import com.generation.desafio_3_carona.model.Passagem;
import com.generation.desafio_3_carona.model.enums.StatusCarona;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CaronaResponseDTO {
        private Long id;
        private String origem;
        private String destino;
        private int vagas;
        private LocalDateTime dataHoraPartida;
        private LocalDateTime dataHoraChegada;
        private double tempoViagem;
        private Integer distanciaKm;
        private int velocidade;
        private BigDecimal valorPorPassageiro;
        private StatusCarona statusCarona;
        private UsuarioDTO motorista;
        private List<PassagemInfoDTO> passagensVendidas; // Renomeado para clareza

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public LocalDateTime getDataHoraPartida() {
        return dataHoraPartida;
    }

    public void setDataHoraPartida(LocalDateTime dataHoraPartida) {
        this.dataHoraPartida = dataHoraPartida;
    }

    public LocalDateTime getDataHoraChegada() {
        return dataHoraChegada;
    }

    public void setDataHoraChegada(LocalDateTime dataHoraChegada) {
        this.dataHoraChegada = dataHoraChegada;
    }

    public double getTempoViagem() {
        return tempoViagem;
    }

    public void setTempoViagem(double tempoViagem) {
        this.tempoViagem = tempoViagem;
    }

    public Integer getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Integer distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }

    public BigDecimal getValorPorPassageiro() {
        return valorPorPassageiro;
    }

    public void setValorPorPassageiro(BigDecimal valorPorPassageiro) {
        this.valorPorPassageiro = valorPorPassageiro;
    }

    public StatusCarona getStatusCarona() {
        return statusCarona;
    }

    public void setStatusCarona(StatusCarona statusCarona) {
        this.statusCarona = statusCarona;
    }

    public UsuarioDTO getMotorista() {
        return motorista;
    }

    public void setMotorista(UsuarioDTO motorista) {
        this.motorista = motorista;
    }

    public List<PassagemInfoDTO> getPassagensVendidas() {
        return passagensVendidas;
    }

    public void setPassagensVendidas(List<PassagemInfoDTO> passagensVendidas) {
        this.passagensVendidas = passagensVendidas;
    }
}
