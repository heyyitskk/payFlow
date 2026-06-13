package com.airtribe.payflow.controller;

import com.airtribe.payflow.entity.Transaction;
import com.airtribe.payflow.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> sendMoney(@RequestBody Map<String, Object> payload) {
        try {
            String senderUPI = (String) payload.get("senderUPI_id");
            String receiverUPI = (String) payload.get("receiverUPI_id");
            Double amount = ((Number) payload.get("amount")).doubleValue();
            String note = (String) payload.get("note");

            if (senderUPI == null || receiverUPI == null || amount == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Missing required fields: senderUPI_id, receiverUPI_id, amount");
            }

            Transaction transaction = transactionService.sendMoney(senderUPI, receiverUPI, amount, note);
            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        if (transaction.isPresent()) {
            return ResponseEntity.ok(transaction.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Transaction not found with ID: " + id);
    }

    @GetMapping("/search/sender")
    public ResponseEntity<?> getTransactionsBySender(@RequestParam String senderUPI) {
        List<Transaction> transactions = transactionService.getTransactionsBySender(senderUPI);
        if (transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No transactions found for sender: " + senderUPI);
        }
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/search/receiver")
    public ResponseEntity<?> getTransactionsByReceiver(@RequestParam String receiverUPI) {
        List<Transaction> transactions = transactionService.getTransactionsByReceiver(receiverUPI);
        if (transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No transactions found for receiver: " + receiverUPI);
        }
        return ResponseEntity.ok(transactions);
    }

}
