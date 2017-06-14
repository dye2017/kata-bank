package com.bank.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by davidye on 14/06/2017.
 */
public class TransferDTO {

    @NotNull
    private Long accountId;

    @NotNull
    private BigDecimal transferAmount;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }


    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
                "accountId=" + accountId +
                ", transferAmount=" + transferAmount +
                '}';
    }
}
