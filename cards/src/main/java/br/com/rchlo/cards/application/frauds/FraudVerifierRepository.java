package br.com.rchlo.cards.application.frauds;

import br.com.rchlo.cards.domain.frauds.FraudVerifier;

import java.util.List;

public interface FraudVerifierRepository {
    List<FraudVerifier> findAllEnabled();
}
