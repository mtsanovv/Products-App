package com.mtsan.techstore.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product implements Serializable {
	public static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "quantity")
	private Long quantity;

	@Column(name = "criticalQuantity")
	private Long criticalQuantity;

	@Column(name = "pricePerItem")
	private BigDecimal pricePerItem;

	public Product() {
	} //default constructor

	public Product(String name, Long quantity, BigDecimal pricePerItem) {
		//the actual usable constructor
		this.name = name;
		this.quantity = quantity;
		this.criticalQuantity = criticalQuantity;
		this.pricePerItem = pricePerItem;
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPricePerItem() {
		return pricePerItem;
	}

	public Long getId() {
		return id;
	}

	public Long getQuantity() {
		return quantity;
	}

	public Long getCriticalQuantity() {
		return criticalQuantity;
	}

	public void setCriticalQuantity(Long criticalQuantity) {
		this.criticalQuantity = criticalQuantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPricePerItem(BigDecimal pricePerItem) {
		this.pricePerItem = pricePerItem;
	}
}
