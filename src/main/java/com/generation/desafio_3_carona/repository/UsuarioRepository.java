package com.generation.desafio_3_carona.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.desafio_3_carona.model.Usuario;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	public Optional<Usuario> findByEmail(String email);
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Usuario u SET u.nome = :nome, u.foto = :foto, u.tipo = :tipo WHERE u.id = :id")
	void atualizarPerfil(
			@Param("id") Long id,
			@Param("nome") String nome,
			@Param("foto") String foto,
			@Param("tipo") String tipo
	);
}
