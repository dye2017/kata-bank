package com.bank.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSFER_TFT")
public class Transfer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "TRANSFER_SEQ_GEN")
	@SequenceGenerator(name = "TRANSFER_SEQ_GEN", sequenceName = "TRANSFER_GEN")
	@Column(name = "TFT_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "ACC_ID", nullable = false)
	@NotNull
	private Account account;

	@Column(name = "TFT_TYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	@NotNull
	private TransferType type;

	@Column(name = "TFT_DATE", nullable = false)
	@NotNull
	private LocalDateTime transferDate;

	@ManyToOne
	@JoinColumn(name = "TFT_AMOUNT", nullable = false)
	@NotNull
	private BigDecimal transferAmount;

	public Transfer() {
	}

	private Transfer(Builder builder) {
		this.id = builder.id;
		this.account = builder.account;
		this.type = builder.type;
		this.transferDate = builder.transferDate;
		this.transferAmount = builder.transferAmount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public TransferType getType() {
		return type;
	}

	public void setType(TransferType type) {
		this.type = type;
	}

	public LocalDateTime getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(LocalDateTime transferDate) {
		this.transferDate = transferDate;
	}

	public BigDecimal getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(BigDecimal transferAmount) {
		this.transferAmount = transferAmount;
	}

	/** Format Builder */
	public static class Builder {

		private Long id;
		private Account account;
		private TransferType type;
		private LocalDateTime transferDate;
		private BigDecimal transferAmount;

		public Builder id(Long id) {
			this.id = id;
			return this;
		}

		public Builder account(Account account) {
			this.account = account;
			return this;
		}

		public Builder type(TransferType type) {
			this.type = type;
			return this;
		}

		public Builder transfertDate(LocalDateTime transferDate) {
			this.transferDate = transferDate;
			return this;
		}

		public Builder transfertAmount(BigDecimal transferAmount) {
			this.transferAmount = transferAmount;
			return this;
		}

		public Transfer build() {
			return new Transfer(this);
		}

	}
}
