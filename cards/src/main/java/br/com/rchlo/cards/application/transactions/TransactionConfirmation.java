package br.com.rchlo.cards.application.transactions;

import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.transactions.Transaction;
import br.com.rchlo.cards.infra.email.EmailSender;
import br.com.rchlo.cards.infra.notifications.NotificationCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TransactionConfirmation {

    private final TransactionRepository transactionRepository;
    private final NotificationCreator notificationCreator;
    private final EmailSender emailSender;

    public TransactionConfirmation(TransactionRepository transactionRepository, NotificationCreator notificationCreator, EmailSender emailSender) {
        this.transactionRepository = transactionRepository;
        this.notificationCreator = notificationCreator;
        this.emailSender = emailSender;
    }

    @Transactional
    public ResponseEntity<Void> confirm(String uuid) {
        Transaction transaction = transactionRepository.findByUuid(uuid).orElseThrow(TransactionNotFoundException::new);
        transaction.confirm();

        Card card = transaction.getCard();
        card.deductFromLimit(transaction.getAmount());

        String notificationText = notificationCreator.createFor(transaction);
        String customerEmail = card.getCustomer().getEmail();
        String subject = "Nova despesa: " + transaction.getDescription();
        emailSender.send(customerEmail, subject, notificationText);

        return ResponseEntity.ok().build();
    }

}
