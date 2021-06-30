package br.com.rchlo.cards.application.frauds;

import br.com.rchlo.cards.application.transactions.TransactionRepository;
import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.cards.CardBuilder;
import br.com.rchlo.cards.domain.frauds.FraudVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TooFastFraudVerificationTest {

    private Card card;
    private TransactionRepository transactionRepository;
    private TooFastFraudVerification fraudVerification;

    @BeforeEach
    void setUp() {
        card = new CardBuilder()
                .withAvailableLimit(new BigDecimal("15000"))
                .build();
        transactionRepository = Mockito.mock(TransactionRepository.class);
        Clock clock = Clock.fixed(Instant.parse("2021-06-30T23:30:30.000Z"), ZoneId.of("America/Sao_Paulo")); // horário UTC
        fraudVerification = new TooFastFraudVerification(clock, transactionRepository);
    }

    @Test
    void shouldConsiderFraudWhenFasterThanDetectionLimit() {
        Mockito.when(transactionRepository.findTimeOfLastConfirmedTransactionForCard(card))
                .thenReturn(Optional.of(LocalDateTime.parse("2021-06-30T20:30:29"))); // horário de Brasília
        assertThrows(FraudDetectedException.class, () -> fraudVerification.verifyFraud(new BigDecimal("15000"), card));
    }

    @Test
    void shouldNotConsiderFraudWhenSlowerThanDetectionLimit() {
        Mockito.when(transactionRepository.findTimeOfLastConfirmedTransactionForCard(card))
                .thenReturn(Optional.of(LocalDateTime.parse("2021-06-30T20:30:00")));
        assertDoesNotThrow(() -> fraudVerification.verifyFraud(new BigDecimal("15000"), card));
    }

    @Test
    void shouldAcceptFraudType() {
        assertFalse(fraudVerification.accept(FraudVerifier.Type.EXPENDS_ALL_LIMIT));
        assertTrue(fraudVerification.accept(FraudVerifier.Type.TOO_FAST));
    }
}