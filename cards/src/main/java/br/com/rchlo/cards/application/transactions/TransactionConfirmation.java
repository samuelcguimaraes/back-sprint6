package br.com.rchlo.cards.application.transactions;

import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.transactions.Transaction;
import br.com.rchlo.cards.dto.transactions.TransactionConfirmedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TransactionConfirmation {
    
    private final TransactionRepository transactionRepository;
    
    private final TransactionStreamConfig.ConfirmedTransactionSource confirmedTransactionSource;
    
    public TransactionConfirmation(TransactionRepository transactionRepository,
                                   TransactionStreamConfig.ConfirmedTransactionSource confirmedTransactionSource) {
        this.transactionRepository = transactionRepository;
        this.confirmedTransactionSource = confirmedTransactionSource;
    }
    
    @Transactional
    public ResponseEntity<Void> confirm(String uuid) {
        Transaction transaction = transactionRepository.findByUuid(uuid).orElseThrow(TransactionNotFoundException::new);
        transaction.confirm();
        
        Card card = transaction.getCard();
        card.deductFromLimit(transaction.getAmount());
        
        final var customer = transaction.getCard().getCustomer();
        final var confirmedTransactionEvent = new TransactionConfirmedEvent(transaction.getDescription(),
                                                                            transaction.getAmount(),
                                                                            customer.getFullName(),
                                                                            customer.getEmail());
        
        final Message<TransactionConfirmedEvent> message = MessageBuilder
                                                                   .withPayload(confirmedTransactionEvent)
                                                                   .build();
        confirmedTransactionSource.confirmedTransactionsTopic().send(message);
        
        return ResponseEntity.ok().build();
    }

}
