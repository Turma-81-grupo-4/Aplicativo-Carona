package com.generation.desafio_3_carona.service;

import org.springframework.stereotype.Service;

import com.generation.desafio_3_carona.model.Carona;
import com.generation.desafio_3_carona.model.Usuario;

@Service
public class RecursoService {
	public void calcularTempo(Carona carona) {
		int velocidade = carona.getVelocidade();
		int distancia = carona.getDistanciaKm();
		if (velocidade > 0) {
			carona.setTempoViagem((double) distancia / velocidade);
		} else {
			carona.setTempoViagem(0.0);
		}
	}

	public static boolean verificarUsuario(Usuario usuario) {
		final String tipoDesejado = "Motorista";
		return tipoDesejado.equals(usuario.getTipo());
	}
}
