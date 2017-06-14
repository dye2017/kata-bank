package com.bank.domain;

import javax.persistence.*;

@Entity
@Table(name = "USER_USR")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "USER_SEQ_GEN")
	@SequenceGenerator(name = "USER_SEQ_GEN", sequenceName = "USER_GEN")
	@Column(name = "USR_ID")
	private Long id;

	@Column(name = "USR_LASTNAME")
	private Long lastName;


	@Column(name = "USR_FIRSTNAME")
	private Long firstName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLastName() {
		return lastName;
	}

	public void setLastName(Long lastName) {
		this.lastName = lastName;
	}

	public Long getFirstName() {
		return firstName;
	}

	public void setFirstName(Long firstName) {
		this.firstName = firstName;
	}
}
