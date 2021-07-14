package br.com.rchlo.notification;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.SubscribableChannel;

@EnableBinding(StreamConfig.ConfirmedTransactionSink.class)
@Configuration
class StreamConfig {
	
	public interface ConfirmedTransactionSink {
		
		String CONFIRMED_TRANSACTIONS_TOPIC = "confirmedTransactionsTopic";
		
		@Input
		SubscribableChannel confirmedTransactionsTopic();
	}
}
