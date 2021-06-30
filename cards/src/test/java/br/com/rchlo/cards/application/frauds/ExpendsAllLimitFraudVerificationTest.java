package br.com.rchlo.cards.application.frauds;

import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.cards.CardBuilder;
import br.com.rchlo.cards.domain.frauds.FraudVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ExpendsAllLimitFraudVerificationTest {

    private Card card;
    private ExpendsAllLimitFraudVerification fraudVerification;

    @BeforeEach
    void setUp() {
        card = new CardBuilder()
                .withAvailableLimit(new BigDecimal("15000"))
                .build();
        fraudVerification = new ExpendsAllLimitFraudVerification();
    }

    @Test
    void shouldConsiderFraudWhenAllLimitIsExpended() {
        assertThrows(FraudDetectedException.class, () -> fraudVerification.verifyFraud(new BigDecimal("15000"), card));
    }

    @Test
    void shouldNotConsiderFraudWhenExpenseIsLessThanTheLimit() {
        assertDoesNotThrow(() -> fraudVerification.verifyFraud(new BigDecimal("5000"), card));
    }

    @Test
    void shouldAcceptFraudType() {
        assertTrue(fraudVerification.accept(FraudVerifier.Type.EXPENDS_ALL_LIMIT));
        assertFalse(fraudVerification.accept(FraudVerifier.Type.TOO_FAST));
    }

}