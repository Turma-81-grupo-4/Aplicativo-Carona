package com.generation.desafio_3_carona.dto;

import java.time.LocalDate;

public class CaronaDTO {
    private Long id;
    private LocalDate dataViagem;
    private String origem;
    private String destino;
    private UsuarioDTO motorista;

    public CaronaDTO() {}
    public CaronaDTO(Long id, LocalDate dataViagem, String origem, String destino, UsuarioDTO motorista) {
        this.id = id;
        this.dataViagem = dataViagem;
        this.origem = origem;
        this.destino = destino;
        this.motorista = motorista;
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
}
