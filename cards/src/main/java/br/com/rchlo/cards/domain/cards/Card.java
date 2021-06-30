package br.com.rchlo.cards.domain.cards;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Card {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String holderName;
    private String number;
    private String expiration;
    private String securityCode;
    private String issuingCompany;

    private BigDecimal monthlyFee;

    private BigDecimal totalLimit;
    private BigDecimal availableLimit;

    @Embedded
    private Customer customer;

    /** @deprecated */
    protected Card() {
    }

    public Card(String holderName, String number, String expiration, String securityCode, String issuingCompany, BigDecimal monthlyFee, BigDecimal totalLimit, BigDecimal availableLimit, Customer customer) {
        this.holderName = holderName;
        this.number = number;
        this.expiration = expiration;
        this.securityCode = securityCode;
        this.issuingCompany = issuingCompany;
        this.monthlyFee = monthlyFee;
        this.totalLimit = totalLimit;
        this.availableLimit = availableLimit;
        this.customer = customer;
    }

    public BigDecimal getAvailableLimit() {
        return availableLimit;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void deductFromLimit(BigDecimal amount) {
        this.availableLimit = availableLimit.subtract(amount);
    }

    public boolean hasAvailableLimit(BigDecimal amount) {
        return this.availableLimit.compareTo(amount) >= 0;
    }

    public boolean expendsAllLimit(BigDecimal amount) {
        return this.availableLimit.compareTo(amount) == 0;
    }

}
