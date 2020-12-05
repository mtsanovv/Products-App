package com.mtsan.techstore.entities;

import com.mtsan.techstore.Rank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "username")
	private String username;
	public static final String usernamePattern = "^.{1,128}$";

	@Column(name = "displayName")
	private String displayName;
	public static final String displayNamePattern = "^.{1,1024}$";

	@Column(name = "password")
	private String password;
	public static final String passwordPattern = "^.{8,50}$";

	@Column(name = "email")
	private String email;

	@Column(name = "rank", columnDefinition = "ENUM('Merchant', 'Administrator')")
	@Enumerated(EnumType.STRING)
	private Rank rank;

	@OneToMany(mappedBy = "merchant")
	private List<Client> clients;

	@OneToMany(mappedBy = "sellingMerchant")
	private List<Sale> sales;

	public User() {
	} //default constructor

	public User(String username, String displayName, String password, String email, Rank rank) {
		this.username = username;
		this.displayName = displayName;
		this.password = password;
		this.email = email;
		this.rank = rank;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}