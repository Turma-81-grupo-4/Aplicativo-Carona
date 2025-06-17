package com.generation.desafio_3_carona.service;

import com.generation.desafio_3_carona.model.Carona;
import com.generation.desafio_3_carona.model.Passagem;
import com.generation.desafio_3_carona.model.Usuario;
import com.generation.desafio_3_carona.model.enums.StatusCarona;
import com.generation.desafio_3_carona.model.enums.StatusPassagem;
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

    @Transactional
    public Passagem criarPassagem(Long caronaId, String emailUsuarioLogado) {
        Usuario passageiro = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado."));

        Carona carona = caronaRepository.findById(caronaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada!"));

        if (carona.getStatusCarona() != StatusCarona.AGENDADA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível comprar passagem para uma carona que não está agendada.");
        }

        if (carona.getMotorista().getId().equals(passageiro.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O motorista não pode comprar uma passagem para a sua própria carona.");
        }

        if (passagemRepository.existsByPassageiroIdAndCaronaId(passageiro.getId(), caronaId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já comprou uma passagem para esta carona.");
        }

        if (carona.getVagas() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Vagas para esta carona estão esgotadas!");
        }
        carona.setVagas(carona.getVagas() - 1);

            Passagem novaPassagem = new Passagem();
        novaPassagem.setCarona(carona);
        novaPassagem.setPassageiro(passageiro);

        novaPassagem.setStatus(StatusPassagem.CONFIRMADA);
        return passagemRepository.save(novaPassagem);
    }
    @Transactional
    public void deletarPassagem(Long id, String emailUsuarioLogado) {

        Passagem passagem = passagemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passagem não encontrada!"));
        if (!passagem.getPassageiro().getEmail().equals(emailUsuarioLogado)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para deletar esta passagem.");
        }
        Carona carona = passagem.getCarona();

        carona.setVagas(carona.getVagas() + 1);


        caronaRepository.save(carona);

        passagemRepository.deleteById(id);
    }
}
