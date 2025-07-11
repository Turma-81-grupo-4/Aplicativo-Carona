package com.generation.desafio_3_carona.dto;

import java.math.BigDecimal;

public class PagamentoRequestDTO {
	    private Long caronaId;
	    private String origem;
	    private String destino;
	    private String nomeCliente;
	    private String emailCliente;
	    private Integer valorEmCentavos;
	    
		public Long getCaronaId() {
			return caronaId;
		}
		public void setCaronaId(Long caronaId) {
			this.caronaId = caronaId;
		}
		public String getNomeCliente() {
			return nomeCliente;
		}
		public void setNomeCliente(String nomeCliente) {
			this.nomeCliente = nomeCliente;
		}
		public String getEmailCliente() {
			return emailCliente;
		}
		public void setEmailCliente(String emailCliente) {
			this.emailCliente = emailCliente;
		}
		public Integer getValorEmCentavos() {
			return valorEmCentavos;
		}
		public void setValorEmCentavos(Integer valorEmCentavos) {
			this.valorEmCentavos = valorEmCentavos;
		}
		public String getOrigem() {
			return origem;
		}
		public void setOrigem(String origem) {
			this.origem = origem;
		}
		public String getDestino() {
			return destino;
		}
		public void setDestino(String destino) {
			this.destino = destino;
		}
	    
		
	    
	    

}
