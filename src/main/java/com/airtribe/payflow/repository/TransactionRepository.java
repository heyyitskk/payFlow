package com.airtribe.payflow.repository;

import com.airtribe.payflow.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.senderUpiId = :senderUpiId")
    List<Transaction> findBySenderUpiId(@Param("senderUpiId") String senderUpiId);

    @Query("SELECT t FROM Transaction t WHERE t.receiverUpiId = :receiverUpiId")
    List<Transaction> findByReceiverUpiId(@Param("receiverUpiId") String receiverUpiId);

}
