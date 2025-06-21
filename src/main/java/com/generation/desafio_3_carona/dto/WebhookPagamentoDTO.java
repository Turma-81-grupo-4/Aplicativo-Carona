package com.generation.desafio_3_carona.dto;

public class WebhookPagamentoDTO {
	    private String status;
	    private Long caronaId;
	    private String emailCliente;
	    private Integer valorEmCentavos;

	    // Getters e Setters
	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	    public Long getCaronaId() {
	        return caronaId;
	    }

	    public void setCaronaId(Long caronaId) {
	        this.caronaId = caronaId;
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

	    

}
