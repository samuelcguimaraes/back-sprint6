package br.com.rchlo.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmedTransactionEvent {
	
	private String description;
	private BigDecimal amount;
	private String fullNameCustomer;
	private String emailCustomer;
	
}
