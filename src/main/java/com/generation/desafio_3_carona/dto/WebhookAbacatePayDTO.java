package com.generation.desafio_3_carona.dto;

import java.util.List;

public class WebhookAbacatePayDTO {
	
	    private String event;
	    private boolean devMode;
	    private Data data;

	    

	    public String getEvent() {
			return event;
		}

		public void setEvent(String event) {
			this.event = event;
		}

		public boolean isDevMode() {
			return devMode;
		}

		public void setDevMode(boolean devMode) {
			this.devMode = devMode;
		}

		public Data getData() {
			return data;
		}

		public void setData(Data data) {
			this.data = data;
		}
		
		

		public static class Data {
	        private Billing billing;

			public Billing getBilling() {
				return billing;
			}

			public void setBilling(Billing billing) {
				this.billing = billing;
			}

	        
	    }

	    public static class Billing {
	        private int paidAmount;
	        private String status;
	        private Customer customer;
	        private List<Product> products;
	        
			public int getPaidAmount() {
				return paidAmount;
			}
			public void setPaidAmount(int paidAmount) {
				this.paidAmount = paidAmount;
			}
			public String getStatus() {
				return status;
			}
			public void setStatus(String status) {
				this.status = status;
			}
			public Customer getCustomer() {
				return customer;
			}
			public void setCustomer(Customer customer) {
				this.customer = customer;
			}
			public List<Product> getProducts() {
				return products;
			}
			public void setProducts(List<Product> products) {
				this.products = products;
			}

	       
	    }

	    public static class Customer {
	        private String email;
	        private Metadata metadata;

			public String getEmail() {
				return email;
			}

			public void setEmail(String email) {
				this.email = email;
			}

			public Metadata getMetadata() {
				return metadata;
			}

			public void setMetadata(Metadata metadata) {
				this.metadata = metadata;
			}

	       
	    }
	    
	    public static class Metadata {
	        private String cellphone;
	        private String email;
	        private String name;
	        private String taxId;
	        
	        

	    public String getCellphone() {
				return cellphone;
			}



			public void setCellphone(String cellphone) {
				this.cellphone = cellphone;
			}



			public String getEmail() {
				return email;
			}



			public void setEmail(String email) {
				this.email = email;
			}



			public String getName() {
				return name;
			}



			public void setName(String name) {
				this.name = name;
			}



			public String getTaxId() {
				return taxId;
			}



			public void setTaxId(String taxId) {
				this.taxId = taxId;
			}
	    }


		public static class Product {
	        private String externalId;

			public String getExternalId() {
				return externalId;
			}

			public void setExternalId(String externalId) {
				this.externalId = externalId;
			}

	        
	    }


}
