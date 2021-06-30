package br.com.rchlo.cards.dto.cards;

import br.com.rchlo.cards.domain.transactions.Transaction;

import java.time.YearMonth;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CardStatisticsResponseDto {

    private Map<YearMonth, CardStatisticsItemResponseDto> data = new TreeMap<>();

    public void addItem(YearMonth yearMonth, List<Transaction> transactionsForYearMonth) {
        DoubleSummaryStatistics transactionSummaryStatistics = transactionsForYearMonth.stream().mapToDouble(transaction -> transaction.getAmount().doubleValue()).summaryStatistics();
        data.put(yearMonth, new CardStatisticsItemResponseDto(transactionSummaryStatistics));
    }

    public Map<YearMonth, CardStatisticsItemResponseDto> getData() {
        return data;
    }
}
