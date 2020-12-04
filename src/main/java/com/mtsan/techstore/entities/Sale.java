package com.mtsan.techstore.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "sales")
public class Sale implements Serializable {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "productId")
	private Product product;

	@Column(name = "quantitySold")
	private Long quantitySold;

	@ManyToOne
	@JoinColumn(name = "soldBy")
	private User sellingMerchant;

	@Column(name = "dateSold")
	private Date dateSold;

	public Sale() {
	} //default constructor

	public Sale(Long id, Product product, Long quantitySold, User sellingMerchant, Date dateSold) {
		this.id = id;
		this.product = product;
		this.quantitySold = quantitySold;
		this.sellingMerchant = sellingMerchant;
		this.dateSold = dateSold;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(Long quantitySold) {
		this.quantitySold = quantitySold;
	}

	public void setSellingMerchant(User sellingMerchant) {
		this.sellingMerchant = sellingMerchant;
	}

	public Date getDateSold() {
		return dateSold;
	}

	public void setDateSold(Date dateSold) {
		this.dateSold = dateSold;
	}
}
