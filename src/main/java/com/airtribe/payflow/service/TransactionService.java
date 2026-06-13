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

    public Transaction sendMoney(String senderUPI, String receiverUPI, Double amount, String note) {
        Optional<User> senderOptional = userRepository.findByPhoneNumber(senderUPI);
        Optional<User> receiverOptional = userRepository.findByPhoneNumber(receiverUPI);

        if (!senderOptional.isPresent()) {
            throw new RuntimeException("Sender not found with UPI: " + senderUPI);
        }
        if (!receiverOptional.isPresent()) {
            throw new RuntimeException("Receiver not found with UPI: " + receiverUPI);
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
        transaction.setSenderUPI_id(senderUPI);
        transaction.setReceiverUPI_id(receiverUPI);
        transaction.setAmount(amount);
        transaction.setNote(note);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsBySender(String senderUPI) {
        return transactionRepository.findBySenderUPI(senderUPI);
    }

    public List<Transaction> getTransactionsByReceiver(String receiverUPI) {
        return transactionRepository.findByReceiverUPI(receiverUPI);
    }

    public Optional<Transaction> getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId);
    }

}
