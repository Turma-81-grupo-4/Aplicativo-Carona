package com.generation.desafio_3_carona.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.generation.desafio_3_carona.model.enums.StatusPassagem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_passagens")
public class Passagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference("usuario_passagens")
    private Usuario passageiro;

    @ManyToOne
    @JoinColumn(name = "carona_id")
    @JsonBackReference("carona_passagens")
    private Carona carona;
@CreationTimestamp
    private LocalDateTime dataReserva;
@NotNull
@Enumerated
    private StatusPassagem status;

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

    public LocalDateTime getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(LocalDateTime dataReserva) {
        this.dataReserva = dataReserva;
    }

    public StatusPassagem getStatus() {
        return status;
    }

    public void setStatus(StatusPassagem status) {
        this.status = status;
    }
}