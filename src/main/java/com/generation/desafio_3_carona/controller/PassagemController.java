package com.generation.desafio_3_carona.controller;

import com.generation.desafio_3_carona.repository.UsuarioRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.desafio_3_carona.model.Passagem;
import com.generation.desafio_3_carona.repository.CaronaRepository;
import com.generation.desafio_3_carona.repository.PassagemRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/passagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PassagemController {

	private final CaronaRepository caronaRepository;

	private final UsuarioRepository usuarioRepository;

	@Autowired
	private PassagemRepository passagemRepository;

	PassagemController(UsuarioRepository usuarioRepository, CaronaRepository caronaRepository) {
		this.usuarioRepository = usuarioRepository;
		this.caronaRepository = caronaRepository;
	}

	@GetMapping
	public ResponseEntity<List<Passagem>> getAll() {
		return ResponseEntity.ok(passagemRepository.findAll());
	}

	@PostMapping("/criar")
	public ResponseEntity<Passagem> criarPassagem(@RequestBody Passagem passagem) {
		if (usuarioRepository.existsById(passagem.getPassageiro().getId())
				&& caronaRepository.existsById(passagem.getCarona().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(passagemRepository.save(passagem));
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Viagem não existe!", null);
	}

}
