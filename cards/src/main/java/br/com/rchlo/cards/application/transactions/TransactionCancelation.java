package br.com.rchlo.cards.application.transactions;

import br.com.rchlo.cards.domain.transactions.Transaction;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class TransactionCancelation {

    private final TransactionRepository transactionRepository;

    public TransactionCancelation(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void cancel(String uuid) {
        Transaction transaction = transactionRepository.findByUuid(uuid).orElseThrow(TransactionNotFoundException::new);
        transaction.cancel();
    }
}
