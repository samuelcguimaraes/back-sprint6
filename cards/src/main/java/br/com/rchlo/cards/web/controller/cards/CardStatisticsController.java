package br.com.rchlo.cards.web.controller.cards;

import br.com.rchlo.cards.application.cards.CardStatisticsCalculator;
import br.com.rchlo.cards.application.cards.InvalidCardException;
import br.com.rchlo.cards.domain.transactions.Transaction;
import br.com.rchlo.cards.dto.cards.CardStatisticsResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CardStatisticsController {

    private final CardStatisticsCalculator cardStatisticsCalculator;

    public CardStatisticsController(CardStatisticsCalculator cardStatisticsCalculator) {
        this.cardStatisticsCalculator = cardStatisticsCalculator;
    }

    @GetMapping("/admin/cards/statistics/{number}")
    public ResponseEntity<CardStatisticsResponseDto> calculateStatistics(@PathVariable("number") String cardNumber) {
        try {
            CardStatisticsResponseDto cardStatistics = cardStatisticsCalculator.calculate(cardNumber);
            return ResponseEntity.ok().body(cardStatistics);
        } catch (InvalidCardException ex) {
            return ResponseEntity.notFound().build();
        }
    }


}
