package com.generation.desafio_3_carona.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_passagens")
public class Passagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Usuario passageiro) {
        this.passageiro = passageiro;
    }

    public Carona getCarona() {
        return carona;
    }

    public void setCarona(Carona carona) {
        this.carona = carona;
    }

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonIgnoreProperties(value = {"passagens", "carona"}, allowSetters = true)
    private Usuario passageiro;

    @ManyToOne
    @JoinColumn(name = "carona_id")
    @JsonIgnoreProperties({"passagensVendidasNestaCarona", "usuario"})
    private Carona carona;
}
