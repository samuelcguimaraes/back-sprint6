package br.com.rchlo.cards.application.cards;

import br.com.rchlo.cards.application.transactions.TransactionRepository;
import br.com.rchlo.cards.domain.transactions.Transaction;
import br.com.rchlo.cards.dto.cards.CardStatisticsResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CardStatisticsCalculator {

    private final TransactionRepository transactionRepository;

    public CardStatisticsCalculator(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public CardStatisticsResponseDto calculate(String cardNumber) {
        List<Transaction> allCardTransactions = transactionRepository.allCardTransactions(cardNumber);

        if (allCardTransactions.isEmpty()) {
            throw new InvalidCardException();
        }

        var cardStatistics = new CardStatisticsResponseDto();
        Map<YearMonth, List<Transaction>> transactionsPerYearMonth = allCardTransactions.stream()
                .collect(Collectors.groupingBy(transaction -> YearMonth.from(transaction.getCreatedAt())));
        for (YearMonth yearMonth : transactionsPerYearMonth.keySet()) {
            List<Transaction> transactionsForYearMonth = transactionsPerYearMonth.get(yearMonth);
            cardStatistics.addItem(yearMonth, transactionsForYearMonth);
        }
        return cardStatistics;

    }
}
