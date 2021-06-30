package br.com.rchlo.cards.application.frauds;

import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.frauds.FraudVerifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExpendsAllLimitFraudVerification implements FraudVerification {
    @Override
    public boolean accept(FraudVerifier.Type fraudVerifierType) {
        return FraudVerifier.Type.EXPENDS_ALL_LIMIT.equals(fraudVerifierType);
    }

    @Override
    public void verifyFraud(BigDecimal amount, Card card) {
        if (card.expendsAllLimit(amount)) {
            throw new FraudDetectedException();
        }
    }
}
