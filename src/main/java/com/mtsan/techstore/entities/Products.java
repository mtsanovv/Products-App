package com.mtsan.techstore.entities;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class Products implements Serializable
{
	public static final long serialVersionUID = 2L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "quantity")
	private Long quantity;

	@Column(name = "pricePerItem")
	private BigDecimal pricePerItem;

	Products() {} //default constructor

	public Products(String name, Long quantity, BigDecimal pricePerItem)
	{
		//the actual usable constructor
		this.name = name;
		this.quantity = quantity;
		this.pricePerItem = pricePerItem;
	}

	public String getName()
	{
		return name;
	}

	public BigDecimal getPricePerItem()
	{
		return pricePerItem;
	}

	public Long getId()
	{
		return id;
	}

	public Long getQuantity()
	{
		return quantity;
	}

	public void setQuantity(Long quantity)
	{
		this.quantity = quantity;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public void setPricePerItem(BigDecimal pricePerItem)
	{
		this.pricePerItem = pricePerItem;
	}
}
