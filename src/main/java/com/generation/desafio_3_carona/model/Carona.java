package com.generation.desafio_3_carona.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_caronas")
public class Carona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@FutureOrPresent(message = "A data da viagem não pode ser no passado.")
	private LocalDate dataViagem;

	@NotBlank(message = "O campo origem é obrigatório.")
	@Size(min = 10, max = 255, message = "O campo origem deve ter entre 10 e 255 caracteres.")
	private String origem;

	@NotBlank(message = "O campo destino é obrigatório.")
	@Size(min = 10, max = 255, message = "O campo destino deve ter entre 10 e 255 caracteres.")
	private String destino;

	@Min(value = 1, message = "A distância deve ser de no mínimo 1 km.")
	private int distancia;

	@Min(value = 1, message = "A velocidade média deve ser maior que zero.")
	private int velocidade;

	@Min(value = 1, message = "O número de vagas deve ser no mínimo 1.")
	private int vagas;
	private double tempoViagem;


	@ManyToOne
	@JoinColumn(name = "usuario_id")
	@JsonBackReference("usuario_caronas")
	@NotNull(message = "O motorista é obrigatório.")
	private Usuario motorista;

	@OneToMany(mappedBy = "carona", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("carona_passagens")
	private List<Passagem> passagensVendidasNestaCarona;


	public List<Passagem> getPassagensVendidasNestaCarona() {
		return passagensVendidasNestaCarona;
	}

	public void setPassagensVendidasNestaCarona(List<Passagem> passagensVendidasNestaCarona) {
		this.passagensVendidasNestaCarona = passagensVendidasNestaCarona;
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

	public int getDistancia() {
		return distancia;
	}

	public void setDistancia(int distancia) {
		this.distancia = distancia;
	}

	public int getVelocidade() {
		return velocidade;
	}

	public void setVelocidade(int velocidade) {
		this.velocidade = velocidade;
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

	public Usuario getMotorista() {
		return motorista;
	}

	public void setMotorista(Usuario motorista) {
		this.motorista = motorista;
	}
}