package com.generation.desafio_3_carona.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import com.generation.desafio_3_carona.dto.PagamentoRequestDTO;
import com.generation.desafio_3_carona.model.Pagamento;
import com.generation.desafio_3_carona.model.Carona;

@Service
public class PagamentoService {

	public String criarCobranca(PagamentoRequestDTO dto) {
	    RestTemplate restTemplate = new RestTemplate();

	    Pagamento request = new Pagamento();
	    request.setFrequency("ONE_TIME");
	    request.setMethods(Collections.singletonList("PIX"));

	    Pagamento.Product product = new Pagamento.Product();
	    product.setExternalId(dto.getCaronaId().toString());
	    product.setName("Comprar Carona");
	    product.setQuantity(1);
	    product.setPrice(dto.getValorEmCentavos());
	    product.setDescription("Carona ID: " + dto.getCaronaId());
	    request.setProducts(Collections.singletonList(product));

	    Pagamento.Customer customer = new Pagamento.Customer();
	    customer.setEmail(dto.getEmailCliente());
	    customer.setName(dto.getNomeCliente());
	    customer.setCellphone("11999999999");
	    customer.setTaxId("11144477735");
	    request.setCustomer(customer);

	    request.setReturnUrl("http://localhost:5173/caronas");
	    request.setCompletionUrl("http://localhost:5173/passagens");

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth("abc_dev_QDnqCxaGB2Lm4hSA2YfqWJf4");

	    HttpEntity<Pagamento> entity = new HttpEntity<>(request, headers);

	    ResponseEntity<String> response = restTemplate.exchange(
	        "https://api.abacatepay.com/v1/billing/create",
	        HttpMethod.POST,
	        entity,
	        String.class
	    );

	    if (!response.getStatusCode().is2xxSuccessful()) {
	        throw new RuntimeException("Erro ao criar cobrança: " + response.getBody());
	    }

	    return response.getBody(); 
	}

}

