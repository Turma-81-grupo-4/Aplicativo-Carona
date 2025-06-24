package com.generation.desafio_3_carona.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import com.generation.desafio_3_carona.model.Carona;
import com.generation.desafio_3_carona.model.Usuario;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class RecursoService {
	public void calcularTempoEChegada(Carona carona) {
		Integer velocidade = carona.getVelocidade();
		Double distancia = carona.getDistanciaKm();
		LocalDateTime partida = carona.getDataHoraPartida();

		if (partida == null || distancia == null || velocidade == null || velocidade <= 0 || distancia <= 0) {
			carona.setTempoViagem(0.0);
			carona.setDataHoraChegada(null);
			return;
		}
		double tempoViagemEmHoras = (double) distancia / velocidade;
		if (velocidade > 0) {
			carona.setTempoViagem(tempoViagemEmHoras);
		} else {
			carona.setTempoViagem(0.0);
		}
		long duracaoEmSegundos = (long)(tempoViagemEmHoras * 3600);
		LocalDateTime dataHoraChegada = partida.plusSeconds(duracaoEmSegundos);
		carona.setDataHoraChegada(dataHoraChegada);
	}

	public static boolean verificarUsuario(Usuario usuario) {
		final String tipoDesejado = "Motorista";
		return tipoDesejado.equals(usuario.getTipo());
	}

	public void calcularChegada(@Valid Carona carona) {

	}
}
