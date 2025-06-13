package com.generation.desafio_3_carona.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.generation.desafio_3_carona.model.enums.StatusCarona;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tb_caronas")
public class Carona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@FutureOrPresent(message = "A data da viagem não pode ser no passado.")
	private LocalDateTime dataHoraPartida;

	private LocalDateTime dataHoraChegada;

	@NotBlank(message = "O campo origem é obrigatório.")
	private String origem;

	@NotBlank(message = "O campo destino é obrigatório.")
	private String destino;

	@Positive( message = "A distância deve ser de no mínimo 1 km.")
	private Integer distanciaKm;

	@Min(value = 1, message = "A velocidade média deve ser maior que zero.")
	private int velocidade;

	@Positive( message = "O número de vagas deve ser no mínimo 1.")
	private int vagas;
	private double tempoViagem;

	@NotNull(message = "O valor por passageiro é obrigatório")
	@DecimalMin(value = "0.0", inclusive = false)
	private BigDecimal valorPorPassageiro;

	@Enumerated(EnumType.STRING)

	private StatusCarona statusCarona;


	@ManyToOne
	@JoinColumn(name = "usuario_id")
	@JsonBackReference("usuario_caronas")
	@NotNull(message = "O motorista é obrigatório.")
	private Usuario motorista;

	@OneToMany(mappedBy = "carona", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference("carona_passagens")
	private List<Passagem> passagensVendidasNestaCarona;

	@PrePersist
	public void prePersist() {
		if (this.statusCarona == null) {
			this.statusCarona = StatusCarona.AGENDADA;
		}
	}

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

	public int getDistanciaKm() {
		return distanciaKm;
	}

	public void setDistanciaKm(int distanciaKm) {
		this.distanciaKm = distanciaKm;
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

	public LocalDateTime getDataHoraPartida() {
		return dataHoraPartida;
	}

	public void setDataHoraPartida(LocalDateTime dataHoraPartida) {
		this.dataHoraPartida = dataHoraPartida;
	}

	public LocalDateTime getDataHoraChegada() {
		return dataHoraChegada;
	}

	public void setDataHoraChegada(LocalDateTime dataHoraChegada) {
		this.dataHoraChegada = dataHoraChegada;
	}

	public void setDistanciaKm(Integer distanciaKm) {
		this.distanciaKm = distanciaKm;
	}

	public BigDecimal getValorPorPassageiro() {
		return valorPorPassageiro;
	}

	public void setValorPorPassageiro(BigDecimal valorPorPassageiro) {
		this.valorPorPassageiro = valorPorPassageiro;
	}

	public StatusCarona getStatusCarona() {
		return statusCarona;
	}

	public void setStatusCarona(StatusCarona statusCarona) {
		this.statusCarona = statusCarona;
	}
}