package br.com.rchlo.cards.application.transactions;

import br.com.rchlo.cards.domain.transactions.Transaction;
import org.springframework.stereotype.Service;

@Service
public class TransactionDetails {

    private final TransactionRepository transactionRepository;

    public TransactionDetails(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction detail(String uuid) {
        return transactionRepository.findByUuid(uuid).orElseThrow(() -> new TransactionNotFoundException());
    }

}
