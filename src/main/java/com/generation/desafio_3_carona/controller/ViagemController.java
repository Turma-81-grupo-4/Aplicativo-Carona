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

import com.generation.desafio_3_carona.model.Viagem;
import com.generation.desafio_3_carona.repository.ViagemRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/viagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ViagemController {
	
	@Autowired
	private ViagemRepository viagemRepository;
	
	@GetMapping
	public ResponseEntity<List<Viagem>> getAll() {
		return ResponseEntity.ok(viagemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Viagem> getById(@PathVariable Long id){
		return viagemRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/tipoviagem/{tipoViagem}")
	public ResponseEntity<List<Viagem>> getByTitulo(@PathVariable String tipoViagem) {
		return ResponseEntity.ok(viagemRepository.findAllByTipoViagemContainingIgnoreCase(tipoViagem));
	}
	
	@PostMapping 
	public ResponseEntity<Viagem> post(@Valid @RequestBody Viagem viagem){
		return ResponseEntity.status(HttpStatus.CREATED).body(viagemRepository.save(viagem));
	}
	
	@PutMapping
    public ResponseEntity<Viagem> put(@Valid @RequestBody Viagem viagem){
        return viagemRepository.findById(viagem.getId())
            .map(resposta -> ResponseEntity.status(HttpStatus.CREATED)
            .body(viagemRepository.save(viagem)))
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Viagem> viagem = viagemRepository.findById(id);
		
		if(viagem.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		viagemRepository.deleteById(id);
	}
}
