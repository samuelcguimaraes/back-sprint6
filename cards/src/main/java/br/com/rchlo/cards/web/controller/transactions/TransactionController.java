package br.com.rchlo.cards.web.controller.transactions;

import br.com.rchlo.cards.application.cards.InvalidCardException;
import br.com.rchlo.cards.application.frauds.FraudDetectedException;
import br.com.rchlo.cards.application.transactions.*;
import br.com.rchlo.cards.domain.transactions.Transaction;
import br.com.rchlo.cards.dto.transactions.TransactionRequestDto;
import br.com.rchlo.cards.dto.transactions.TransactionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
public class TransactionController {

    private final TransactionDetails transactionDetails;
    private final TransactionCreation transactionCreation;
    private final TransactionConfirmation transactionConfirmation;
    private final TransactionCancelation transactionCancelation;

    public TransactionController(TransactionDetails transactionDetails, TransactionCreation transactionCreation,
                                 TransactionConfirmation transactionConfirmation, TransactionCancelation transactionCancelation) {
        this.transactionDetails = transactionDetails;
        this.transactionCreation = transactionCreation;
        this.transactionConfirmation = transactionConfirmation;
        this.transactionCancelation = transactionCancelation;
    }

    @GetMapping("/transactions/{uuid}")
    public ResponseEntity<TransactionResponseDto> detail(@PathVariable("uuid") String uuid) {
        Transaction transaction = transactionDetails.detail(uuid);
        return ResponseEntity.ok().body(new TransactionResponseDto(transaction));
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponseDto> create(@RequestBody @Valid TransactionRequestDto transactionRequest, UriComponentsBuilder uriBuilder) {
        try {
            Transaction transaction = transactionCreation.create(transactionRequest);
            URI uri = uriBuilder.path("/transactions/{uuid}").buildAndExpand(transaction.getUuid()).toUri();
            return ResponseEntity.created(uri).body(new TransactionResponseDto(transaction));
        } catch (InvalidCardException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid card");
        } catch (LimitNotAvailableException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit not available");
        } catch (FraudDetectedException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fraud detected");
        }
    }

    @Transactional
    @PutMapping("/transactions/{uuid}")
    public ResponseEntity<Void> confirm(@PathVariable("uuid") String uuid) {
        try {
            transactionConfirmation.confirm(uuid);
        } catch (TransactionNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (InvalidTransactionStatusException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction status");
        }
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/transactions/{uuid}")
    public ResponseEntity<Void> cancel(@PathVariable("uuid") String uuid) {
        try {
            transactionCancelation.cancel(uuid);
        } catch (TransactionNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (InvalidTransactionStatusException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction status");
        }
        return ResponseEntity.ok().build();
    }
}
