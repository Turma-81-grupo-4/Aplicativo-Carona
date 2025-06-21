package com.generation.desafio_3_carona.controller;

import com.generation.desafio_3_carona.dto.WebhookAbacatePayDTO;
import com.generation.desafio_3_carona.model.Carona;
import com.generation.desafio_3_carona.model.Passagem;
import com.generation.desafio_3_carona.model.Usuario;
import com.generation.desafio_3_carona.model.enums.StatusPassagem;
import com.generation.desafio_3_carona.repository.CaronaRepository;
import com.generation.desafio_3_carona.repository.PassagemRepository;
import com.generation.desafio_3_carona.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper; // Adicione este import
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/webhook")
public class WebhookPagamentoController {

    @Value("${ABACATEPAY_WEBHOOK_SECRET}")
    private String expectedSecret;

    private final UsuarioRepository usuarioRepository;
    private final CaronaRepository caronaRepository;
    private final PassagemRepository passagemRepository;

    public WebhookPagamentoController(UsuarioRepository usuarioRepository, CaronaRepository caronaRepository, PassagemRepository passagemRepository) {
        this.usuarioRepository = usuarioRepository;
        this.caronaRepository = caronaRepository;
        this.passagemRepository = passagemRepository;
    }

    @PostMapping("/abacatepay")
    public ResponseEntity<String> receberWebhook(
            @RequestBody WebhookAbacatePayDTO payload,
            @RequestParam("webhookSecret") String webhookSecret) {

        System.out.println("🚨 Webhook recebido!");

        if (!expectedSecret.equals(webhookSecret)) {
            System.out.println("❌ Webhook rejeitado: secret inválida");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Secret inválida");
        }

        String email = null;
        if (payload.getData() != null &&
            payload.getData().getBilling() != null &&
            payload.getData().getBilling().getCustomer() != null &&
            payload.getData().getBilling().getCustomer().getMetadata() != null) {
            email = payload.getData().getBilling().getCustomer().getMetadata().getEmail();
        }

        Long caronaId = null;
        if (payload.getData() != null &&
            payload.getData().getBilling() != null &&
            payload.getData().getBilling().getProducts() != null &&
            !payload.getData().getBilling().getProducts().isEmpty() &&
            payload.getData().getBilling().getProducts().get(0).getExternalId() != null) {
            try {
                caronaId = Long.parseLong(payload.getData().getBilling().getProducts().get(0).getExternalId());
            } catch (NumberFormatException e) {
                System.err.println("Erro ao converter externalId para Long: " + e.getMessage());
                return ResponseEntity.badRequest().body("ID da carona inválido.");
            }
        }


        if (email == null || caronaId == null) {
            System.out.println("❌ Webhook rejeitado: Dados incompletos (email ou caronaId nulo). Email: " + email + ", Carona ID: " + caronaId);
            return ResponseEntity.badRequest().body("Dados incompletos");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        Optional<Carona> caronaOpt = caronaRepository.findById(caronaId);

        System.out.println("DEBUG Busca - Usuario encontrado? " + usuarioOpt.isPresent());
        System.out.println("DEBUG Busca - Carona encontrada? " + caronaOpt.isPresent());

        if (usuarioOpt.isEmpty() || caronaOpt.isEmpty()) {
            System.out.println("❌ Webhook rejeitado: Usuário ou carona não encontrado. Email: " + email + ", Carona ID: " + caronaId);
            return ResponseEntity.badRequest().body("Usuário ou carona não encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        Carona carona = caronaOpt.get();

        boolean passagemExiste = passagemRepository.existsByPassageiro_IdAndCarona_Id(usuario.getId(), carona.getId());
        System.out.println("DEBUG Existência - Passagem já existe? " + passagemExiste);

        if (passagemExiste) {
            System.out.println("❌ Webhook rejeitado: Passagem já existe para este passageiro (" + usuario.getId() + ") e carona (" + carona.getId() + ").");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Passagem já existe");
        }

        System.out.println("DEBUG Vagas - Vagas atuais na Carona " + carona.getId() + ": " + carona.getVagas());
        if (carona.getVagas() <= 0) {
            System.out.println("❌ Webhook rejeitado: Carona lotada. ID: " + carona.getId());
            return ResponseEntity.badRequest().body("Carona lotada");
        }

        Passagem novaPassagem = new Passagem();
        novaPassagem.setPassageiro(usuario);
        novaPassagem.setCarona(carona);
        novaPassagem.setDataReserva(LocalDateTime.now());
        novaPassagem.setStatus(StatusPassagem.CONFIRMADA); 

        try {
            passagemRepository.save(novaPassagem);
            System.out.println("✅ Passagem salva com sucesso! ID: " + novaPassagem.getId());

            carona.setVagas(carona.getVagas() - 1);
            caronaRepository.save(carona);
            System.out.println("✅ Vagas da carona atualizadas com sucesso! Carona ID: " + carona.getId() + ", Novas vagas: " + carona.getVagas());

            return ResponseEntity.ok("Webhook processado com sucesso! Passagem criada e vagas atualizadas.");
        } catch (Exception e) {
            System.err.println("❌ Erro ao salvar passagem ou atualizar carona: " + e.getMessage());
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao processar pagamento.");
        }
    }
}