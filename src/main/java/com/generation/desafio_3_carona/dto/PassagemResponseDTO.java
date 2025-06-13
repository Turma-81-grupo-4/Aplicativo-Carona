package com.generation.desafio_3_carona.dto;

public class PassagemResponseDTO {
    private Long id;
    private UsuarioDTO passageiro;
    private CaronaResponseDTO carona;

    public PassagemResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UsuarioDTO getPassageiro() { return passageiro; }
    public void setPassageiro(UsuarioDTO passageiro) { this.passageiro = passageiro; }
    public CaronaResponseDTO getCarona() { return carona; }
    public void setCarona(CaronaResponseDTO carona) { this.carona = carona; }
}