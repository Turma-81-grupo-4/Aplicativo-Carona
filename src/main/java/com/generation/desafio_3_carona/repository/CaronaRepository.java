package com.generation.desafio_3_carona.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.desafio_3_carona.model.Carona;

public interface CaronaRepository extends JpaRepository<Carona, Long>{
	public List<Carona> findAllByDestinoContainingIgnoreCase(@Param("destino") String destino);
}
