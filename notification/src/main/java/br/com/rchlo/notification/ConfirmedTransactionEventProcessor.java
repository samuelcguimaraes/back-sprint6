package br.com.rchlo.notification;

import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class ConfirmedTransactionEventProcessor {
	
	private final EmailSender emailSender;
	private final NotificationCreator notificationCreator;
	
	@StreamListener(StreamConfig.ConfirmedTransactionSink.CONFIRMED_TRANSACTIONS_TOPIC)
	void confirmedTransaction(ConfirmedTransactionEvent confirmedTransactionEvent) {
		
		String notificationText = notificationCreator.createFor(confirmedTransactionEvent);
		String customerEmail = confirmedTransactionEvent.getEmailCustomer();
		String subject = "Nova despesa: " + confirmedTransactionEvent.getDescription();
		emailSender.send(customerEmail, subject, notificationText);
	}
}
