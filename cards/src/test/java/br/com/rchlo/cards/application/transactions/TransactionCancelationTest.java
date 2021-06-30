package br.com.rchlo.cards.application.transactions;

import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.cards.CardBuilder;
import br.com.rchlo.cards.domain.cards.Customer;
import br.com.rchlo.cards.domain.transactions.Transaction;
import br.com.rchlo.cards.infra.email.EmailSender;
import br.com.rchlo.cards.infra.notifications.NotificationCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionCancelationTest {

    private TransactionRepository transactionRepository;
    private TransactionCancelation transactionCancelation;

    @BeforeEach
    void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        transactionCancelation = new TransactionCancelation(transactionRepository);
    }

    @Test
    void shouldCancelTransaction() {
        Card card = new CardBuilder().build();
        Transaction transaction = new Transaction(new BigDecimal("500"), "Fuzzy Cardigan", card);
        Mockito.when(transactionRepository.findByUuid("1234")).thenReturn(Optional.of(transaction));

        transactionCancelation.cancel("1234");

        assertEquals(Transaction.Status.CANCELED, transaction.getStatus());
    }

    @Test
    void shouldThrowWhenTransactionNotFound() {
        assertThrows(TransactionNotFoundException.class, () -> transactionCancelation.cancel("1234"));
        Mockito.verify(transactionRepository).findByUuid("1234");
    }

    @Test
    void shouldThrowWhenTransactionAlreadyCanceled() {
        Card card = new CardBuilder().build();;
        Transaction transaction = new Transaction(new BigDecimal("500"), "Fuzzy Cardigan", card);
        transaction.cancel();

        Mockito.when(transactionRepository.findByUuid("1234")).thenReturn(Optional.of(transaction));

        assertThrows(InvalidTransactionStatusException.class, () -> transactionCancelation.cancel("1234"));
    }
}