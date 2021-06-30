package br.com.rchlo.cards.application.frauds;

import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.frauds.FraudVerifier;

import java.math.BigDecimal;

public interface FraudVerification {

    boolean accept(FraudVerifier.Type fraudVerifierType);
    void verifyFraud(BigDecimal amount, Card card);

}
