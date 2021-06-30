package br.com.rchlo.cards.application.transactions;

import br.com.rchlo.cards.application.cards.CardRepository;
import br.com.rchlo.cards.application.cards.InvalidCardException;
import br.com.rchlo.cards.application.frauds.FraudVerificationManager;
import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.transactions.Transaction;
import br.com.rchlo.cards.dto.transactions.TransactionRequestDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class TransactionCreation {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final FraudVerificationManager fraudVerificationManager;

    public TransactionCreation(TransactionRepository transactionRepository, CardRepository cardRepository, FraudVerificationManager fraudVerificationManager) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.fraudVerificationManager = fraudVerificationManager;
    }

    @Transactional
    public Transaction create(TransactionRequestDto transactionRequest) {
        Optional<Card> possibleCard = cardRepository.findValidCard(transactionRequest.getCardNumber(), transactionRequest.getCardHolderName(), transactionRequest.getCardExpiration().toString(), transactionRequest.getCardSecurityCode());
        Card card = possibleCard.orElseThrow(InvalidCardException::new);

        if(!card.hasAvailableLimit(transactionRequest.getAmount())) {
            throw new LimitNotAvailableException();
        }

        fraudVerificationManager.verifyFrauds(transactionRequest.getAmount(), card);

        Transaction transaction = transactionRequest.asEntity(card);
        transactionRepository.save(transaction);

        return transaction;
    }

}
