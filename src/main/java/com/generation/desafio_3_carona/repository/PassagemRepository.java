package com.generation.desafio_3_carona.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.desafio_3_carona.model.Passagem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PassagemRepository extends JpaRepository<Passagem, Long> {

    @Query("SELECT p FROM Passagem p WHERE p.passageiro.id = :usuarioId")
    List<Passagem> findAllByUsuarioId(@Param("usuarioId") Long usuarioId);
    boolean existsByPassageiroIdAndCaronaId(Long passageiroId, Long caronaId);
    List<Passagem> findAllByPassageiro_Id(Long passageiroId);
}
