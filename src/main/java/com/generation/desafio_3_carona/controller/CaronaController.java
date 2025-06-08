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

public class CaronaController {

    private final RecursoService recursoService;

    private final CaronaRepository caronaRepository;

    private final UsuarioRepository usuarioRepository;

    private final PassagemRepository passagemRepository;

    CaronaController(RecursoService recursoService, CaronaRepository caronaRepository, UsuarioRepository usuarioRepository, PassagemRepository passagemRepository) {
        this.recursoService = recursoService;
        this.caronaRepository = caronaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passagemRepository = passagemRepository;
    }

    @GetMapping
    public ResponseEntity<List<Carona>> getAll() {
        return ResponseEntity.ok(caronaRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carona> getById(@PathVariable Long id) {
        return caronaRepository.findById(id).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada"));
    }

    @GetMapping("/destino/{destino}")
    public ResponseEntity<List<Carona>> getByDestino(@PathVariable String destino) {
        List<Carona> caronasEncontradas = caronaRepository.findAllByDestinoContainingIgnoreCase(destino);
        if (caronasEncontradas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino não encontrado");
        }
        return ResponseEntity.ok(caronasEncontradas);
    }

    @PostMapping
    public ResponseEntity<Carona> post(@Valid @RequestBody Carona carona) {
        if (usuarioRepository.existsById(carona.getMotorista().getId())) {
            recursoService.calcularTempo(carona);
            return ResponseEntity.status(HttpStatus.CREATED).body(caronaRepository.save(carona));
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não existe!", null);
    }

    @PutMapping
    public ResponseEntity<Carona> put(@Valid @RequestBody Carona carona) {
        if (caronaRepository.existsById(carona.getId()) && usuarioRepository.existsById(carona.getMotorista().getId())) {
            recursoService.calcularTempo(carona);
            return ResponseEntity.status(HttpStatus.OK).body(caronaRepository.save(carona));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Carona não existe", null);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Carona> carona = caronaRepository.findById(id);

        if (carona.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        caronaRepository.deleteById(id);

    }
}
