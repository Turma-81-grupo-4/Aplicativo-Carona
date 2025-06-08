package com.generation.desafio_3_carona.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tb_caronas")
public class Carona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDate dataViagem;
	private String origem;
	private String destino;
	private int distancia;
	private int velocidade;
	private int vagas;
	private double tempoViagem;


	@ManyToOne
	@JoinColumn(name = "usuario_id")
	@JsonBackReference("usuario_caronas")
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