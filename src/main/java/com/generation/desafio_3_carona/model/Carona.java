package com.generation.desafio_3_carona.model;

import java.awt.List;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table (name = "tb_caronas")
public class Carona {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(min = 10, max = 255)
	private String destino;
	
	@NotBlank
	@Size(min = 10, max = 255)
	private String origem;
	
	@NotNull
	private int vagas;
	
	@NotNull
	private LocalDate dataViagem;
	
	@NotNull
	private int distancia;
	
	@NotNull
	private int velocidade;
	
	private double tempoViagem;
	
	@ManyToOne
	@JsonIgnoreProperties("carona")
	private Viagem viagem; 
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "carona", cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties("carona")
	private List<Usuario> usuario;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public int getVagas() {
		return vagas;
	}

	public void setVagas(int vagas) {
		this.vagas = vagas;
	}

	public LocalDate getDataViagem() {
		return dataViagem;
	}

	public void setDataViagem(LocalDate dataViagem) {
		this.dataViagem = dataViagem;
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

	public double getTempoViagem() {
		return tempoViagem;
	}

	public void setTempoViagem(double tempoViagem) {
		this.tempoViagem = tempoViagem;
	}

	public Viagem getViagem() {
		return viagem;
	}

	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}

	public List<Usuario> getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}
