package br.com.rchlo.cards.application.transactions;

import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.cards.CardBuilder;
import br.com.rchlo.cards.domain.cards.Customer;
import br.com.rchlo.cards.domain.transactions.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionConfirmationTest {
    
    private TransactionRepository transactionRepository;
    private TransactionConfirmation transactionConfirmation;
    
    private TransactionStreamConfig.ConfirmedTransactionSource confirmedTransactionSource;
    
    @BeforeEach
    void setUp() {
        transactionRepository = Mockito.mock(TransactionRepository.class);
        confirmedTransactionSource = Mockito.mock(TransactionStreamConfig.ConfirmedTransactionSource.class);
        transactionConfirmation = new TransactionConfirmation(transactionRepository, confirmedTransactionSource);
    }
    
    @Test
    void shouldConfirmTransaction() {
        Customer customer = new Customer("Anderson Manuel", "Rua Vergueiro, SP", "ander@son.com");
        Card card = new CardBuilder()
                            .withAvailableLimit(new BigDecimal("1500"))
                            .withCustomer(customer)
                            .build();
        
        Transaction transaction = new Transaction(new BigDecimal("500"), "Fuzzy Cardigan", card);
        Mockito.when(transactionRepository.findByUuid("1234")).thenReturn(Optional.of(transaction));
        Mockito.when(confirmedTransactionSource.confirmedTransactionsTopic()).thenReturn((message, l) -> true);
        
        transactionConfirmation.confirm("1234");
        
        assertEquals(Transaction.Status.CONFIRMED, transaction.getStatus());
        assertEquals(new BigDecimal("1000"), card.getAvailableLimit());
    }

    @Test
    void shouldThrowWhenTransactionNotFound() {
        assertThrows(TransactionNotFoundException.class, () -> transactionConfirmation.confirm("1234"));
        Mockito.verify(transactionRepository).findByUuid("1234");
    }

    @Test
    void shouldThrowWhenTransactionAlreadyConfirmed() {
        Card card = new CardBuilder().build();
        Transaction transaction = new Transaction(new BigDecimal("500"), "Fuzzy Cardigan", card);
        transaction.confirm();

        Mockito.when(transactionRepository.findByUuid("1234")).thenReturn(Optional.of(transaction));

        assertThrows(InvalidTransactionStatusException.class, () -> transactionConfirmation.confirm("1234"));
    }
}