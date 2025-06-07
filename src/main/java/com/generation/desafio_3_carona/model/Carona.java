package com.generation.desafio_3_carona.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_caronas")
public class Carona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private LocalDate dataViagem;

	@NotBlank
	@Size(min = 10, max = 255)
	private String origem;

	@NotBlank
	@Size(min = 10, max = 255)
	private String destino;

	@NotNull
	private int distancia;

	@NotNull
	private int velocidade;

	@NotNull
	private int vagas;

	private double tempoViagem;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario motorista;

	@OneToMany(mappedBy = "carona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties("carona")
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
