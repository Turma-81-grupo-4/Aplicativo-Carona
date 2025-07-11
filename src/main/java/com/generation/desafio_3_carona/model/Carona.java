package com.generation.desafio_3_carona.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
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
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonProperty("data_hora_partida")
	private LocalDateTime dataHoraPartida;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@JsonProperty("data_hora_chegada")
	private LocalDateTime dataHoraChegada;

	@NotBlank(message = "O campo origem é obrigatório.")
	private String origem;

	@NotBlank(message = "O campo destino é obrigatório.")
	private String destino;

	@Positive( message = "A distância deve ser de no mínimo 1 km.")
	@JsonProperty("distancia_km")
	private Double distanciaKm;

	@Min(value = 1, message = "A velocidade média deve ser maior que zero.")
	private Integer velocidade;

	@Min(value = 0, message = "O número de vagas não pode ser negativo.")
	private int vagas;
	private double tempoViagem;

	@NotNull(message = "O valor por passageiro é obrigatório")
	@DecimalMin(value = "0.0", inclusive = false)
	@JsonProperty("valor_por_passageiro")
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

	public double getDistanciaKm() {

		return distanciaKm;
	}

	public void setDistanciaKm(double distanciaKm) {
		this.distanciaKm = distanciaKm;
	}

	public Integer getVelocidade() {
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

	public void setDistanciaKm(Double distanciaKm) {
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