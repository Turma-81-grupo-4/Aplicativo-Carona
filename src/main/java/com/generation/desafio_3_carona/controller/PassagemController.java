package com.generation.desafio_3_carona.controller;

import com.generation.desafio_3_carona.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

import com.generation.desafio_3_carona.service.RecursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.generation.desafio_3_carona.model.Passagem;
import com.generation.desafio_3_carona.repository.CaronaRepository;
import com.generation.desafio_3_carona.repository.PassagemRepository;

@RestController
@RequestMapping("/passagens")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PassagemController {

    private final CaronaRepository caronaRepository;

    private final UsuarioRepository usuarioRepository;

    private final PassagemRepository passagemRepository;

    private final RecursoService recursoService;

    PassagemController(UsuarioRepository usuarioRepository, CaronaRepository caronaRepository, PassagemRepository passagemRepository, RecursoService recursoService) {
        this.usuarioRepository = usuarioRepository;
        this.caronaRepository = caronaRepository;
        this.passagemRepository = passagemRepository;
        this.recursoService = recursoService;
    }

    @GetMapping
    public ResponseEntity<List<Passagem>> getAll() {
        List<Passagem> passagens = passagemRepository.findAll();
        return ResponseEntity.ok(passagemRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Passagem> getById(@PathVariable Long id) {
        return passagemRepository.findById(id).map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passagem não encontrada"));
    }

    @PostMapping("/criar")
    public ResponseEntity<Passagem> criarPassagem(@RequestBody Passagem passagem) {
        if (usuarioRepository.existsById(passagem.getPassageiro().getId())
                && caronaRepository.existsById(passagem.getCarona().getId()))
            return ResponseEntity.status(HttpStatus.CREATED).body(passagemRepository.save(passagem));
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário ou carona não existem!", null);
    }

    @PutMapping
    public ResponseEntity<Passagem> updatePassagem(@RequestBody Passagem passagem) {
        if (usuarioRepository.existsById(passagem.getPassageiro().getId()))
            return ResponseEntity.status(HttpStatus.CREATED).body(passagemRepository.save(passagem));
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passagem não exite!", null);
    }

    @DeleteMapping({"/{id}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassagem(@PathVariable Long id) {
        Optional<Passagem> passagem = passagemRepository.findById(id);
        if (passagem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        passagemRepository.deleteById(id);
    }
}
