package com.mtsan.techstore.entities;

import com.mtsan.techstore.Rank;

import javax.persistence.*;
import java.io.Serializable;

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

	@Column(name = "displayName")
	private String displayName;

	@Column(name = "password")
	private String password;

	@Column(name = "rank", columnDefinition = "ENUM('Merchant', 'Administrator')")
	@Enumerated(EnumType.STRING)
	private Rank rank;

	public User() {
	} //default constructor

	public User(String username, String displayName, String password, Rank rank) {
		this.username = username;
		this.displayName = displayName;
		this.password = password;
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
}