package br.com.rchlo.cards.repository.frauds;

import br.com.rchlo.cards.application.frauds.FraudVerifierRepository;
import br.com.rchlo.cards.domain.frauds.FraudVerifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class FraudVerifierRepositoryWithJpa implements FraudVerifierRepository {

    private final EntityManager entityManager;

    public FraudVerifierRepositoryWithJpa(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<FraudVerifier> findAllEnabled() {
        return entityManager.createQuery("select fv from FraudVerifier fv where fv.enabled = true", FraudVerifier.class)
                .getResultList();
    }
}
