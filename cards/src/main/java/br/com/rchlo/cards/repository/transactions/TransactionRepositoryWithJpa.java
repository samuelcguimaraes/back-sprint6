package br.com.rchlo.cards.repository.transactions;

import br.com.rchlo.cards.application.transactions.TransactionRepository;
import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.transactions.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepositoryWithJpa implements TransactionRepository {

    private final EntityManager entityManager;

    public TransactionRepositoryWithJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Transaction> findByUuid(String uuid) {
        return entityManager.createQuery("select t from Transaction t where t.uuid = :uuid", Transaction.class)
                .setParameter("uuid", uuid)
                .getResultStream().findFirst();
    }

    @Override
    public List<Transaction> allCardTransactions(String cardNumber) {
        return entityManager.createQuery("select t from Transaction t where t.card.number = :number ", Transaction.class)
                .setParameter("number", cardNumber)
                .getResultList();
    }

    @Override
    public Optional<LocalDateTime> findTimeOfLastConfirmedTransactionForCard(Card card) {
        try {
            String jpql = "select max(t.createdAt) from Transaction t where t.card = :card and t.status = :status";
            LocalDateTime timeOfLastConfirmedTransactionForCard = entityManager.createQuery(jpql, LocalDateTime.class)
                    .setParameter("card", card)
                    .setParameter("status", Transaction.Status.CONFIRMED).getSingleResult();
            if (timeOfLastConfirmedTransactionForCard == null) {
                return Optional.empty();
            }
            return Optional.of(timeOfLastConfirmedTransactionForCard);
        } catch (NoResultException ex) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void save(Transaction transaction) {
        entityManager.persist(transaction);
    }

}
