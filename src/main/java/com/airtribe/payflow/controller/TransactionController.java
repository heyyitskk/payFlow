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
            String senderUpiId = (String) payload.get("senderUpiId");
            String receiverUpiId = (String) payload.get("receiverUpiId");
            Double amount = ((Number) payload.get("amount")).doubleValue();
            String note = (String) payload.get("note");

            if (senderUpiId == null || receiverUpiId == null || amount == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Missing required fields: senderUpiId, receiverUpiId, amount");
            }

            Transaction transaction = transactionService.sendMoney(senderUpiId, receiverUpiId, amount, note);
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
    public ResponseEntity<?> getTransactionsBySender(@RequestParam String senderUpiId) {
        List<Transaction> transactions = transactionService.getTransactionsBySender(senderUpiId);
        if (transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No transactions found for sender: " + senderUpiId);
        }
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/search/receiver")
    public ResponseEntity<?> getTransactionsByReceiver(@RequestParam String receiverUpiId) {
        List<Transaction> transactions = transactionService.getTransactionsByReceiver(receiverUpiId);
        if (transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No transactions found for receiver: " + receiverUpiId);
        }
        return ResponseEntity.ok(transactions);
    }

}
