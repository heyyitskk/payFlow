package com.airtribe.payflow.service;

import com.airtribe.payflow.entity.Transaction;
import com.airtribe.payflow.entity.User;
import com.airtribe.payflow.repository.TransactionRepository;
import com.airtribe.payflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public Transaction sendMoney(String senderUpiId, String receiverUpiId, Double amount, String note) {
        Optional<User> senderOptional = userRepository.findByUpiId(senderUpiId);
        Optional<User> receiverOptional = userRepository.findByUpiId(receiverUpiId);

        if (!senderOptional.isPresent()) {
            throw new RuntimeException("Sender not found with UPI ID: " + senderUpiId);
        }
        if (!receiverOptional.isPresent()) {
            throw new RuntimeException("Receiver not found with UPI ID: " + receiverUpiId);
        }

        User sender = senderOptional.get();
        User receiver = receiverOptional.get();

        if (sender.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance. Available: " + sender.getBalance());
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction transaction = new Transaction();
        transaction.setSenderUpiId(senderUpiId);
        transaction.setReceiverUpiId(receiverUpiId);
        transaction.setAmount(amount);
        transaction.setNote(note);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsBySender(String senderUpiId) {
        return transactionRepository.findBySenderUpiId(senderUpiId);
    }

    public List<Transaction> getTransactionsByReceiver(String receiverUpiId) {
        return transactionRepository.findByReceiverUpiId(receiverUpiId);
    }

    public Optional<Transaction> getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

}
