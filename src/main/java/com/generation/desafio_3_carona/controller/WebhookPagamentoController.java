package com.generation.desafio_3_carona.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.generation.desafio_3_carona.dto.WebhookAbacatePayDTO;
import com.generation.desafio_3_carona.model.*;
import com.generation.desafio_3_carona.model.enums.StatusPassagem;
import com.generation.desafio_3_carona.repository.CaronaRepository;
import com.generation.desafio_3_carona.repository.PassagemRepository;
import com.generation.desafio_3_carona.repository.UsuarioRepository;

@RestController
@RequestMapping("/webhook")
public class WebhookPagamentoController {

    @Value("${ABACATEPAY_WEBHOOK_SECRET}")
    private String expectedSecret;

    private final UsuarioRepository usuarioRepository;
    private final CaronaRepository caronaRepository;
    private final PassagemRepository passagemRepository;

    public WebhookPagamentoController(
            UsuarioRepository usuarioRepository,
            CaronaRepository caronaRepository,
            PassagemRepository passagemRepository) {
        this.usuarioRepository = usuarioRepository;
        this.caronaRepository = caronaRepository;
        this.passagemRepository = passagemRepository;
    }

    @PostMapping("/abacatepay")
    public ResponseEntity<String> receberWebhook(
            @RequestBody WebhookAbacatePayDTO payload,
            @RequestParam("webhookSecret") String webhookSecret) {

        System.out.println("🚨 Webhook recebido!");
        System.out.println("event: " + payload.getEvent());
        System.out.println("email: " + payload.getData().getBilling().getCustomer().getEmail());
        System.out.println("caronaId: " + payload.getData().getBilling().getProducts().get(0).getExternalId());

        if (!expectedSecret.equals(webhookSecret)) {
            System.out.println("❌ Webhook rejeitado: secret inválida");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Secret inválida");
        }

        if (!"billing.paid".equalsIgnoreCase(payload.getEvent())) {
            return ResponseEntity.ok("Evento ignorado");
        }

        WebhookAbacatePayDTO.Billing billing = payload.getData().getBilling();
        if (billing == null || !"PAID".equalsIgnoreCase(billing.getStatus())) {
            return ResponseEntity.ok("Status não é PAID");
        }

        String email = billing.getCustomer() != null ? billing.getCustomer().getEmail() : null;
        if (email == null || billing.getProducts() == null || billing.getProducts().isEmpty()) {
            return ResponseEntity.badRequest().body("Dados incompletos");
        }

        String caronaIdStr = billing.getProducts().get(0).getExternalId();
        if (caronaIdStr == null) {
            return ResponseEntity.badRequest().body("CaronaId nulo");
        }

        Long caronaId;
        try {
            caronaId = Long.parseLong(caronaIdStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("CaronaId inválido");
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        Optional<Carona> caronaOpt = caronaRepository.findById(caronaId);

        if (usuarioOpt.isEmpty() || caronaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário ou carona não encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        Carona carona = caronaOpt.get();

        if (passagemRepository.existsByPassageiroIdAndCaronaId(usuario.getId(), carona.getId())) {
            return ResponseEntity.ok("Passagem já existe");
        }

        Passagem nova = new Passagem();
        nova.setCarona(carona);
        nova.setPassageiro(usuario);
        nova.setStatus(StatusPassagem.PENDENTE_PAGAMENTO);
        passagemRepository.save(nova);

        System.out.println("✅ Passagem criada via webhook para " + email);
        return ResponseEntity.ok("Passagem criada");
    }
}