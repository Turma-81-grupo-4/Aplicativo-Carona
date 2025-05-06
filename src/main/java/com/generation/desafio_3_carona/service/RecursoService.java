package com.generation.desafio_3_carona.service;

import org.springframework.stereotype.Service;

import com.generation.desafio_3_carona.model.Carona;

@Service
public class RecursoService {
     public void calcularTempo(Carona carona) {
            int velocidade = carona.getVelocidade();
            int distancia = carona.getDistancia();
            double tempoEmHoras = (double) distancia / velocidade;
            carona.setTempoViagem((int) Math.round(tempoEmHoras));
        }
}
