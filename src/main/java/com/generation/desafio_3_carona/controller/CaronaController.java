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

    private final PassagemRepository passagemRepository;

    CaronaController(RecursoService recursoService, CaronaRepository caronaRepository, UsuarioRepository usuarioRepository, PassagemRepository passagemRepository) {
        this.recursoService = recursoService;
        this.caronaRepository = caronaRepository;
        this.usuarioRepository = usuarioRepository;
        this.passagemRepository = passagemRepository;
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
        dto.setDataViagem(carona.getDataViagem());
        dto.setTempoViagem(carona.getTempoViagem());
        dto.setDistancia(carona.getDistancia());
        dto.setVelocidade(carona.getVelocidade());

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
            dto.setPassagemVendidaNessaCarona(passagensInfo);
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
        if (usuarioRepository.existsById(carona.getMotorista().getId())) {
            recursoService.calcularTempo(carona);

            Carona caronaSalva = caronaRepository.save(carona);

            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(caronaSalva));
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não existe!", null);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaronaResponseDTO> put(@PathVariable Long id, @Valid @RequestBody CaronaUpdateDTO caronaUpdateDto) {

        System.out.println("--- INICIANDO PUT para Carona ID: " + id + " ---");

        Carona caronaExistente = caronaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada"));
        System.out.println("1. Carona encontrada no banco de dados.");

        String usuarioEmail = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        System.out.println("2. Usuário logado: " + usuarioEmail);

        if (!caronaExistente.getMotorista().getEmail().equals(usuarioEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não tem permissão para alterar esta carona.");
        }
        System.out.println("3. Permissão de usuário verificada com sucesso.");

        caronaExistente.setDataViagem(caronaUpdateDto.getDataViagem());
        caronaExistente.setOrigem(caronaUpdateDto.getOrigem());
        caronaExistente.setDestino(caronaUpdateDto.getDestino());
        caronaExistente.setDistancia(caronaUpdateDto.getDistancia());
        caronaExistente.setVelocidade(caronaUpdateDto.getVelocidade());
        caronaExistente.setVagas(caronaUpdateDto.getVagas());
        System.out.println("4. Dados da carona atualizados no objeto 'caronaExistente'.");

        try {
            System.out.println("5. PRESTES A CHAMAR recursoService.calcularTempo...");
            recursoService.calcularTempo(caronaExistente);
            System.out.println("6. ...CHAMADA a recursoService.calcularTempo CONCLUÍDA.");
        } catch (Exception e) {
            System.err.println("!!! ERRO ao chamar recursoService.calcularTempo: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro no cálculo do tempo de viagem.", e);
        }

        System.out.println("7. PRESTES A CHAMAR caronaRepository.save...");
        Carona caronaSalva = caronaRepository.save(caronaExistente);
        System.out.println("8. ...CHAMADA a caronaRepository.save CONCLUÍDA.");

        CaronaResponseDTO dtoResposta = convertToDto(caronaSalva);
        System.out.println("9. Resposta DTO criada.");

        System.out.println("--- FIM do PUT com sucesso ---");
        return ResponseEntity.ok(dtoResposta);
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
