package com.generation.desafio_3_carona.dto;

import com.generation.desafio_3_carona.model.Passagem;
import java.time.LocalDate;
import java.util.List;

public class CaronaResponseDTO {
    private Long id;
    private LocalDate dataViagem;
    private String origem;
    private String destino;
    private int vagas;
    private double tempoViagem;
    private UsuarioDTO motorista;
    private List<Passagem> passagemVendidaNessaCarona;
    private double distancia;
    private int velocidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
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

    public int getVagas() {
        return vagas;
    }

    public void setVagas(int vagas) {
        this.vagas = vagas;
    }

    public double getTempoViagem() {
        return tempoViagem;
    }

    public void setTempoViagem(double tempoViagem) {
        this.tempoViagem = tempoViagem;
    }

    public UsuarioDTO getMotorista() {
        return motorista;
    }

    public void setMotorista(UsuarioDTO motorista) {
        this.motorista = motorista;
    }

    public List<Passagem> getPassagemVendidaNessaCarona() {
        return passagemVendidaNessaCarona;
    }

    public void setPassagemVendidaNessaCarona(List<Passagem> passagemVendidaNessaCarona) {
        this.passagemVendidaNessaCarona = passagemVendidaNessaCarona;
    }
}
