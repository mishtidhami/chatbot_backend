package com.upcl.chatbot.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_number", unique = true, nullable = false, length = 50)
    private String accountNumber;

    @Column(name = "consumer_name", length = 100)
    private String consumerName;

    @Column(name = "mobile_number", length = 15)
    private String mobileNumber;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "meter_number", length = 50)
    private String meterNumber;

    @Column(name = "connection_type", length = 50)
    private String connectionType;

    @Column(name = "load_kw", precision = 5, scale = 2)
    private BigDecimal loadKw;

    @Column(name = "connection_status", length = 50)
    private String connectionStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
