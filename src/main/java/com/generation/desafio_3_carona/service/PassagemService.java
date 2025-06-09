package com.generation.desafio_3_carona.service;

import com.generation.desafio_3_carona.model.Carona;
import com.generation.desafio_3_carona.model.Passagem;
import com.generation.desafio_3_carona.model.Usuario;
import com.generation.desafio_3_carona.repository.CaronaRepository;
import com.generation.desafio_3_carona.repository.PassagemRepository;
import com.generation.desafio_3_carona.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class PassagemService {

    private final PassagemRepository passagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final CaronaRepository caronaRepository;

    public PassagemService(PassagemRepository passagemRepository, UsuarioRepository usuarioRepository, CaronaRepository caronaRepository) {
        this.passagemRepository = passagemRepository;
        this.usuarioRepository = usuarioRepository;
        this.caronaRepository = caronaRepository;
    }

    public Passagem criarPassagem(Long caronaId, String emailUsuarioLogado) {
        Usuario passageiro = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado."));

        Carona carona = caronaRepository.findById(caronaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada!"));

        if (carona.getMotorista().getId().equals(passageiro.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O motorista não pode comprar uma passagem para a sua própria carona.");
        }

        if (passagemRepository.existsByPassageiroIdAndCaronaId(passageiro.getId(), caronaId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já comprou uma passagem para esta carona.");
        }

        Passagem novaPassagem = new Passagem();
        novaPassagem.setCarona(carona);
        novaPassagem.setPassageiro(passageiro);

        return passagemRepository.save(novaPassagem);
    }
@Transactional
    public void deletarPassagem(Long id) {
    System.out.println(">>> BACKEND SERVICE: Verificando existência do ID: " + id);
        if(!passagemRepository.existsById(id)){
            System.out.println(">>> BACKEND SERVICE: ID " + id + " NÃO ENCONTRADO! Lançando erro 404.");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Passagem não encontrada!");
        }
    System.out.println(">>> BACKEND SERVICE: ID " + id + " encontrado. Deletando...");
        passagemRepository.deleteById(id);
    }
}