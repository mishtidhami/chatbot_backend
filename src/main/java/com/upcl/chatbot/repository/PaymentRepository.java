package com.upcl.chatbot.repository;

import com.upcl.chatbot.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByAccountNumberOrderByPaymentDateDesc(String accountNumber);

    List<Payment> findByAccountNumberAndStatus(String accountNumber, String status);
}
