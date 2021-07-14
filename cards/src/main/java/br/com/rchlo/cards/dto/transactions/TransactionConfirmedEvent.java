package br.com.rchlo.cards.dto.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionConfirmedEvent {
	
	private String description;
	private BigDecimal amount;
	private String fullNameCustomer;
	private String emailCustomer;
	
}
