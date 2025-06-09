package com.generation.desafio_3_carona.dto;

public class PassagemResponseDTO {
    private Long id;
    private UsuarioDTO passageiro;
    private CaronaDTO carona;

    public PassagemResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UsuarioDTO getPassageiro() { return passageiro; }
    public void setPassageiro(UsuarioDTO passageiro) { this.passageiro = passageiro; }
    public CaronaDTO getCarona() { return carona; }
    public void setCarona(CaronaDTO carona) { this.carona = carona; }
}