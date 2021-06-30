package br.com.rchlo.cards.application.frauds;

import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.frauds.FraudVerifier;
import br.com.rchlo.cards.application.transactions.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class TooFastFraudVerification implements FraudVerification {

    private final Clock clock;
    private final TransactionRepository transactionRepository;

    public TooFastFraudVerification(Clock clock, TransactionRepository transactionRepository) {
        this.clock = clock;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public boolean accept(FraudVerifier.Type fraudVerifierType) {
        return FraudVerifier.Type.TOO_FAST.equals(fraudVerifierType);
    }

    @Override
    public void verifyFraud(BigDecimal amount, Card card) {
        Optional<LocalDateTime> possibleTimeForLastTransaction = transactionRepository.findTimeOfLastConfirmedTransactionForCard(card);
        if (possibleTimeForLastTransaction.isPresent()) {
            LocalDateTime timeForLastTransaction = possibleTimeForLastTransaction.get();
            if(ChronoUnit.SECONDS.between(timeForLastTransaction, LocalDateTime.now(clock)) < 30) {
                throw new FraudDetectedException();
            }
        }
    }
}
