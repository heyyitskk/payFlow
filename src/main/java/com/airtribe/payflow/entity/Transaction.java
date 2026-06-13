package com.airtribe.payflow.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false)
    private String senderUPI_id;

    @Column(nullable = false)
    private String receiverUPI_id;

    @Column(nullable = false)
    private Double amount;

    @Column
    private String note;

}
