package br.com.rchlo.cards.application.cards;

import br.com.rchlo.cards.domain.cards.Card;

import java.util.Optional;

public interface CardRepository {
    Optional<Card> findValidCard(String number, String holderName, String expiration, String securityCode);
}
