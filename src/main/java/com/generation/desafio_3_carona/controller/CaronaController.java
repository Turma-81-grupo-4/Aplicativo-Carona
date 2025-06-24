package com.generation.desafio_3_carona.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.generation.desafio_3_carona.dto.CaronaResponseDTO;
import com.generation.desafio_3_carona.dto.PassagemInfoDTO;
import com.generation.desafio_3_carona.dto.UsuarioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
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

import com.generation.desafio_3_carona.dto.CaronaUpdateDTO;
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
    private static final Logger log = LoggerFactory.getLogger(CaronaController.class);
    CaronaController(RecursoService recursoService, CaronaRepository caronaRepository, UsuarioRepository usuarioRepository) {
        this.recursoService = recursoService;
        this.caronaRepository = caronaRepository;
        this.usuarioRepository = usuarioRepository;

    }

    @GetMapping
    public ResponseEntity<List<CaronaResponseDTO>> getAll() {
        List<CaronaResponseDTO> caronasDto = caronaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(caronasDto);
    }

     @GetMapping("/{id}")
    public ResponseEntity<CaronaResponseDTO> getById(@PathVariable Long id) {
        return caronaRepository.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada"));
    }
    private CaronaResponseDTO convertToDto(Carona carona) {
        CaronaResponseDTO dto = new CaronaResponseDTO();
        dto.setId(carona.getId());
        dto.setOrigem(carona.getOrigem());
        dto.setDestino(carona.getDestino());
        dto.setVagas(carona.getVagas());
        dto.setDataHoraPartida(carona.getDataHoraPartida());
        dto.setDataHoraChegada(carona.getDataHoraChegada());
        dto.setTempoViagem(carona.getTempoViagem());
        dto.setDistanciaKm(carona.getDistanciaKm());
        dto.setVelocidade(carona.getVelocidade());
        dto.setValorPorPassageiro(carona.getValorPorPassageiro());
        dto.setStatusCarona(carona.getStatusCarona());

        if (carona.getMotorista() != null) {
            UsuarioDTO motoristaDto = new UsuarioDTO(
                    carona.getMotorista().getId(),
                    carona.getMotorista().getNome(),
                    carona.getMotorista().getFoto()
            );
            dto.setMotorista(motoristaDto);
        }

        if (carona.getPassagensVendidasNestaCarona() != null) {
            List<PassagemInfoDTO> passagensInfo = carona.getPassagensVendidasNestaCarona().stream()
                    .map(passagem -> {
                        UsuarioDTO passageiroDto = null;
                        if (passagem.getPassageiro() != null) {
                            passageiroDto = new UsuarioDTO(
                                    passagem.getPassageiro().getId(),
                                    passagem.getPassageiro().getNome(),
                                    passagem.getPassageiro().getFoto()
                            );
                        }
                        return new PassagemInfoDTO(passagem.getId(), passageiroDto);
                    })
                    .collect(Collectors.toList());
            dto.setPassagensVendidas(passagensInfo);
        }
        return dto;
    }

    @GetMapping("/destino/{destino}")
    public ResponseEntity<List<CaronaResponseDTO>> getByDestino(@PathVariable String destino) {
        List<Carona> caronasEncontradas = caronaRepository.findAllByDestinoContainingIgnoreCase(destino);
        if (caronasEncontradas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Destino não encontrado");
        }

        List<CaronaResponseDTO> caronasDto = caronasEncontradas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(caronasDto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CaronaResponseDTO> post(@Valid @RequestBody Carona carona) {
        log.info("Valor de dataHoraPartida recebido após JSON binding: {}", carona.getDataHoraPartida());
        if (!usuarioRepository.existsById(carona.getMotorista().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário motorista não existe!", null);
        }

        try {
            recursoService.calcularTempoEChegada(carona);

            Carona caronaSalva = caronaRepository.save(carona);

            CaronaResponseDTO dtoResposta = convertToDto(caronaSalva);

            return ResponseEntity.status(HttpStatus.CREATED).body(dtoResposta);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno ao processar a carona.", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carona> put(@PathVariable Long id, @Valid @RequestBody CaronaUpdateDTO caronaUpdateDto) {
        Carona caronaExistente = caronaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada"));

        String usuarioEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        if (!caronaExistente.getMotorista().getEmail().equals(usuarioEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem permissão para alterar esta carona.");
        }

        caronaExistente.setDataHoraPartida(caronaUpdateDto.getDataHoraPartida());
        caronaExistente.setOrigem(caronaUpdateDto.getOrigem());
        caronaExistente.setDestino(caronaUpdateDto.getDestino());
        caronaExistente.setDistanciaKm(caronaUpdateDto.getDistanciaKm());
        caronaExistente.setVelocidade(caronaUpdateDto.getVelocidade());
        caronaExistente.setVagas(caronaUpdateDto.getVagas());
        caronaExistente.setValorPorPassageiro(caronaUpdateDto.getValorPorPassageiro());
        caronaExistente.setStatusCarona(caronaUpdateDto.getStatusCarona());

        recursoService.calcularTempoEChegada(caronaExistente);

        Carona caronaAtualizada = caronaRepository.save(caronaExistente);

        return ResponseEntity.ok(caronaAtualizada);
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
