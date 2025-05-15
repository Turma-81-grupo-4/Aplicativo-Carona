//package com.generation.desafio_3_carona.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.query.Param;
//
//import com.generation.desafio_3_carona.model.Viagem;
//
//public interface ViagemRepository extends JpaRepository<Viagem, Long> {
//	public List<Viagem> findAllByTipoViagemContainingIgnoreCase(@Param("tipoViagem") String tipoViagem);
//}
