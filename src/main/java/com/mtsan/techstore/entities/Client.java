package com.mtsan.techstore.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "clients")
public class Client implements Serializable {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;
	public static final String namePattern = "^.{1,1024}$";

	@ManyToOne
	@JoinColumn(name = "merchantId")
	private User merchant;

	public Client() {
	} //default constructor

	public Client(Long id, String name, User merchant) {
		this.id = id;
		this.name = name;
		this.merchant = merchant;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMerchant(User merchant) {
		this.merchant = merchant;
	}
}
