package com.generation.desafio_3_carona.controller;

import com.generation.desafio_3_carona.dto.CaronaDTO;
import com.generation.desafio_3_carona.dto.PassagemResponseDTO;
import com.generation.desafio_3_carona.dto.UsuarioDTO;
import com.generation.desafio_3_carona.model.Carona;
import com.generation.desafio_3_carona.model.Usuario;
import com.generation.desafio_3_carona.repository.UsuarioRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.generation.desafio_3_carona.service.RecursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.generation.desafio_3_carona.model.Passagem;
import com.generation.desafio_3_carona.repository.CaronaRepository;
import com.generation.desafio_3_carona.repository.PassagemRepository;

@RestController
@RequestMapping("/passagens")

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
    public ResponseEntity<List<PassagemResponseDTO>> getAll() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String emailUsuarioLogado = authentication.getName();

            Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(emailUsuarioLogado);

            if (usuarioOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Usuario usuario = usuarioOptional.get();
            Long usuarioId = usuario.getId();

            List<Passagem> passagensDoUsuario = passagemRepository.findAllByUsuarioId(usuarioId);


            if (passagensDoUsuario.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<PassagemResponseDTO> passagensDTO = passagensDoUsuario.stream()
                    .map(this::converterParaDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(passagensDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private PassagemResponseDTO converterParaDTO(Passagem passagem) {
        PassagemResponseDTO passagemDTO = new PassagemResponseDTO();
        passagemDTO.setId(passagem.getId());

        Carona carona = passagem.getCarona();
        if (carona != null) {
            CaronaDTO caronaDTO = new CaronaDTO();
            caronaDTO.setId(carona.getId());
            caronaDTO.setOrigem(carona.getOrigem());
            caronaDTO.setDestino(carona.getDestino());
            caronaDTO.setDataViagem(carona.getDataViagem());

            Usuario motorista = carona.getMotorista();
            if (motorista != null) {
                caronaDTO.setMotorista(new UsuarioDTO(motorista.getId(), motorista.getNome(), motorista.getFoto()));
            }
            passagemDTO.setCarona(caronaDTO);
        }

        Usuario passageiro = passagem.getPassageiro();
        if (passageiro != null) {
            passagemDTO.setPassageiro(new UsuarioDTO(passageiro.getId(), passageiro.getNome(), passageiro.getFoto()));
        }

        return passagemDTO;
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
        if (usuarioRepository.existsById(passagem.getPassageiro().getId()) && passagemRepository.existsById(passagem.getId()))
            return ResponseEntity.status(HttpStatus.CREATED).body(passagemRepository.save(passagem));
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passagem ou usuário não exite!", null);
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
