package com.bank.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ACCOUNT_ACC")
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "ACCOUNT_SEQ_GEN")
	@SequenceGenerator(name = "ACCOUNT_SEQ_GEN", sequenceName = "ACCOUNT_GEN")
	@Column(name = "ACC_ID")
	private Long id;

	@Column(name = "ACC_BALANCE", nullable = false)
	@NotNull
	private BigDecimal balance;

	@ManyToOne
	@JoinColumn(name = "USR_ID", nullable = false)
	@NotNull
	private User owner;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "account", fetch = FetchType.LAZY)
	private Set<Transfer> transfers = new HashSet<>();

	public Account() {
	}

	private Account(Builder builder) {
		this.id = builder.id;
		this.balance = builder.balance;
		this.owner = builder.owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<Transfer> getTransfers() {
		return transfers;
	}

	public void setTransfers(Set<Transfer> transfers) {
		this.transfers = transfers;
	}

	public void addTransfer(Transfer transfer) {
		this.transfers.add(transfer);

	}
	public void updateBalance(Transfer transfer) {
		setBalance(getBalance().add(transfer.getTransferAmount()));
	}

	public static class Builder {

		private Long id;
		private User owner;
		private BigDecimal balance;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder owner(User owner) {
			this.owner = owner;
			return this;
		}

		public Builder balance(BigDecimal balance) {
			this.balance = balance;
			return this;
		}

		public Account build() {
			return new Account(this);
		}

	}

}
