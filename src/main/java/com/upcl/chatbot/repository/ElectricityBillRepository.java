package com.upcl.chatbot.repository;

import com.upcl.chatbot.model.ElectricityBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ElectricityBillRepository extends JpaRepository<ElectricityBill, Integer> {

    List<ElectricityBill> findByAccountNumberOrderByBillYearDescBillMonthDesc(String accountNumber);

    Optional<ElectricityBill> findTopByAccountNumberOrderByBillYearDescBillMonthDesc(String accountNumber);

    List<ElectricityBill> findByAccountNumberAndBillStatus(String accountNumber, String billStatus);
}
