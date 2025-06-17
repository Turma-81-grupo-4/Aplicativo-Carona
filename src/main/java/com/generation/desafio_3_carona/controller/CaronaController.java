package com.generation.desafio_3_carona.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.generation.desafio_3_carona.dto.CaronaResponseDTO;
import com.generation.desafio_3_carona.dto.PassagemInfoDTO;
import com.generation.desafio_3_carona.dto.UsuarioDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    CaronaController(RecursoService recursoService, CaronaRepository caronaRepository, UsuarioRepository usuarioRepository) {
        this.recursoService = recursoService;
        this.caronaRepository = caronaRepository;
        this.usuarioRepository = usuarioRepository;

    }

    @GetMapping
    public ResponseEntity<List<CaronaResponseDTO>> getAll() {
        List<Carona> caronas = caronaRepository.findAll();
        
        for (Carona carona : caronas) {
        	recursoService.atualizarStatusCaronaAutomaticamente(carona);
        }


        List<CaronaResponseDTO> caronasDto = caronas.stream()
        		.map(this::convertToDto)
        		.collect(Collectors.toList());
        return ResponseEntity.ok(caronasDto);
    }

     @GetMapping("/{id}")
    public ResponseEntity<CaronaResponseDTO> getById(@PathVariable Long id) {
        Carona carona = caronaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada"));
    
        recursoService.atualizarStatusCaronaAutomaticamente(carona);

        return ResponseEntity.ok(convertToDto(carona));
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
    public ResponseEntity<CaronaResponseDTO> post(@Valid @RequestBody Carona carona) {
        usuarioRepository.findById(carona.getMotorista().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário motorista não existe!"));

        recursoService.calcularTempoEChegada(carona);
        recursoService.atualizarStatusCaronaAutomaticamente(carona);

        Carona novaCarona = caronaRepository.save(carona);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(novaCarona));
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

        recursoService.calcularTempoEChegada(caronaExistente);
        recursoService.atualizarStatusCaronaAutomaticamente(caronaExistente);

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
