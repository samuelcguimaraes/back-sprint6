package br.com.rchlo.cards.application.transactions;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;

@EnableBinding(TransactionStreamConfig.ConfirmedTransactionSource.class)
@Configuration
public class TransactionStreamConfig {
	
	public interface ConfirmedTransactionSource {
		
		@Output
		MessageChannel confirmedTransactionsTopic();
	}
}
