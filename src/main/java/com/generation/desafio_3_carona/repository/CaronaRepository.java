package com.generation.desafio_3_carona.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.desafio_3_carona.model.Carona;

public interface CaronaRepository extends JpaRepository<Carona, Long>{
	
	 @Override
	    @EntityGraph(attributePaths = {
	        "motorista",
	        "passagensVendidasNestaCarona",
	        "passagensVendidasNestaCarona.passageiro"
	    })
	    List<Carona> findAll();
	 
	   @Override
	    @EntityGraph(attributePaths = {
	        "motorista",
	        "passagensVendidasNestaCarona",
	        "passagensVendidasNestaCarona.passageiro"
	    })
	    Optional<Carona> findById(Long id);
	   
	    @EntityGraph(attributePaths = {
	            "motorista",
	            "passagensVendidasNestaCarona",
	            "passagensVendidasNestaCarona.passageiro"
	        })
	    	List<Carona> findAllByDestinoContainingIgnoreCase(@Param("destino") String destino);
	    }

