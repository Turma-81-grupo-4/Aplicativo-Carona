package com.generation.desafio_3_carona.dto;

public class PassagemInfoDTO {
    private Long id;
    private UsuarioDTO passageiro;


    public PassagemInfoDTO() {
    }

    public PassagemInfoDTO(Long id, UsuarioDTO passageiro) {
        this.id = id;
        this.passageiro = passageiro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioDTO getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(UsuarioDTO passageiro) {
        this.passageiro = passageiro;
    }
}