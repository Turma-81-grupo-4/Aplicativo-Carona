package com.generation.desafio_3_carona.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.desafio_3_carona.model.Carona;
import com.generation.desafio_3_carona.repository.CaronaRepository;
import com.generation.desafio_3_carona.repository.PassagemRepository;
import com.generation.desafio_3_carona.repository.UsuarioRepository;
import com.generation.desafio_3_carona.service.RecursoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/caronas")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CaronaController {

	private final RecursoService recursoService;

	@Autowired
	private CaronaRepository caronaRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PassagemRepository passagemRepository;

	CaronaController(RecursoService recursoService) {
		this.recursoService = recursoService;
	}

	@GetMapping
	public ResponseEntity<List<Carona>> getAll() {

		List<Carona> caronas = caronaRepository.findAll();

		for (Carona carona : caronas) {
			recursoService.calcularTempo(carona);
		}
		return ResponseEntity.ok(caronas);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Carona> getById(@PathVariable Long id) {

		Optional<Carona> caronaOptional = caronaRepository.findById(id);

		if (caronaOptional.isPresent()) {
			Carona carona = caronaOptional.get();
			recursoService.calcularTempo(carona);
			return ResponseEntity.ok(carona);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@GetMapping("/destino/{destino}")
	public ResponseEntity<List<Carona>> getByDestino(@PathVariable String destino) {

		List<Carona> caronas = caronaRepository.findAllByDestinoContainingIgnoreCase(destino);

		for (Carona carona : caronas) {
			recursoService.calcularTempo(carona);
		}
		return ResponseEntity.ok(caronas);
	}

	@PostMapping
	public ResponseEntity<Carona> post(@Valid @RequestBody Carona carona) {
		if (usuarioRepository.existsById(carona.getMotorista().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(caronaRepository.save(carona));

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Viagem não existe!", null);
	}

//	@PutMapping
//	public ResponseEntity<Carona> put(@Valid @RequestBody Carona carona) {
//		if (viagemRepository.existsById(carona.getId())) {
//			if (viagemRepository.existsById(carona.getViagem().getId()))
//				return ResponseEntity.status(HttpStatus.OK).body(caronaRepository.save(carona));
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Viagem não existe", null);
//		}
//
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Carona> carona = caronaRepository.findById(id);

		if (carona.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		caronaRepository.deleteById(id);

	}
}
