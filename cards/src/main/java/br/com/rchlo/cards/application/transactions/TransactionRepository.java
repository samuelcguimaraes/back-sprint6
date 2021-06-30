package br.com.rchlo.cards.application.transactions;

import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.transactions.Transaction;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> findByUuid(String uuid);

    List<Transaction> allCardTransactions(String cardNumber);

    Optional<LocalDateTime> findTimeOfLastConfirmedTransactionForCard(Card card);

    @Transactional
    void save(Transaction transaction);
}
