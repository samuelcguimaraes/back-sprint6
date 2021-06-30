package br.com.rchlo.cards.repository.cards;

import br.com.rchlo.cards.application.cards.CardRepository;
import br.com.rchlo.cards.domain.cards.Card;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class CardRepositoryWithJpa implements CardRepository {

    private final EntityManager entityManager;

    public CardRepositoryWithJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Card> findValidCard(String number, String holderName, String expiration, String securityCode) {
       return entityManager.createQuery("select c from Card c where c.number = :number " +
                " and c.holderName = :name and c.expiration = :expiration and c.securityCode = :code", Card.class)
                .setParameter("number", number)
                .setParameter("name", holderName)
                .setParameter("expiration", expiration)
                .setParameter("code", securityCode)
                .getResultStream().findFirst();
    }
}
