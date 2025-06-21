package com.generation.desafio_3_carona.model;

import java.math.BigDecimal;
import java.util.List;

public class Pagamento {
	
	    private String frequency;
	    private List<String> methods;
	    private List<Product> products;
	    private String returnUrl;
	    private String completionUrl;
	    private Customer customer;

	    public static class Product {
	        private String externalId;
	        private String name;
	        private int quantity;
	        private Integer price;
	        private String description;
	        
	        
			public String getExternalId() {
				return externalId;
			}
			public void setExternalId(String externalId) {
				this.externalId = externalId;
			}
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public int getQuantity() {
				return quantity;
			}
			public void setQuantity(int quantity) {
				this.quantity = quantity;
			}
			public Integer getPrice() {
				return price;
			}
			public void setPrice(Integer price) {
				this.price = price;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}

	        
	    }

	    public static class Customer {
	        private String email;
	        private String name;
	        private String cellphone;
	        private String taxId;
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
			public String getCellphone() {
				return cellphone;
			}
			public void setCellphone(String cellphone) {
				this.cellphone = cellphone;
			}
			public String getTaxId() {
				return taxId;
			}
			public void setTaxId(String taxId) {
				this.taxId = taxId;
			}

	        
	    }

		public String getFrequency() {
			return frequency;
		}

		public void setFrequency(String frequency) {
			this.frequency = frequency;
		}

		public List<String> getMethods() {
			return methods;
		}

		public void setMethods(List<String> methods) {
			this.methods = methods;
		}

		public List<Product> getProducts() {
			return products;
		}

		public void setProducts(List<Product> products) {
			this.products = products;
		}

		public String getReturnUrl() {
			return returnUrl;
		}

		public void setReturnUrl(String returnUrl) {
			this.returnUrl = returnUrl;
		}

		public String getCompletionUrl() {
			return completionUrl;
		}

		public void setCompletionUrl(String completionUrl) {
			this.completionUrl = completionUrl;
		}

		public Customer getCustomer() {
			return customer;
		}

		public void setCustomer(Customer customer) {
			this.customer = customer;
		}

}
