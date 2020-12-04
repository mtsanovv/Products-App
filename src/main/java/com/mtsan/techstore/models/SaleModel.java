package com.mtsan.techstore.models;

public class SaleModel {
	private Long productId;
	private Long quantitySold;

	public SaleModel() {
	}

	public SaleModel(Long productId, Long quantitySold) {
		this.productId = productId;
		this.quantitySold = quantitySold;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(Long quantitySold) {
		this.quantitySold = quantitySold;
	}
}
