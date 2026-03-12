package com.upcl.chatbot.repository;

import com.upcl.chatbot.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {

    List<Complaint> findByAccountNumberOrderByCreatedAtDesc(String accountNumber);

    List<Complaint> findByAccountNumberAndStatus(String accountNumber, String status);
}
