package br.com.rchlo.cards.application.frauds;

import br.com.rchlo.cards.domain.cards.Card;
import br.com.rchlo.cards.domain.frauds.FraudVerifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FraudVerificationManager {

    private final FraudVerifierRepository fraudVerifierRepository;
    private final List<FraudVerification> fraudVerifications;

    public FraudVerificationManager(FraudVerifierRepository fraudVerifierRepository, List<FraudVerification> fraudVerifications) {
        this.fraudVerifierRepository = fraudVerifierRepository;
        this.fraudVerifications = fraudVerifications;
    }

    public void verifyFrauds(BigDecimal amount, Card card) {
        List<FraudVerifier> enabledFraudVerifiers = fraudVerifierRepository.findAllEnabled();

        for (FraudVerifier fraudVerifier : enabledFraudVerifiers) {
            for (FraudVerification verification : fraudVerifications) {
                if (verification.accept(fraudVerifier.getType())) {
                   verification.verifyFraud(amount, card);
                }
            }
        }

    }
}
