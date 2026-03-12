package com.upcl.chatbot.service;

import com.upcl.chatbot.dto.CustomerContext;
import com.upcl.chatbot.model.*;
import com.upcl.chatbot.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepo;
    private final ElectricityBillRepository billRepo;
    private final ComplaintRepository complaintRepo;
    private final PaymentRepository paymentRepo;

    public Optional<CustomerContext> buildContext(String accountNumber) {

        Optional<Customer> customerOpt = customerRepo.findByAccountNumber(accountNumber.trim());
        if (customerOpt.isEmpty()) {
            log.warn("Account not found: {}", accountNumber);
            return Optional.empty();
        }

        Customer customer = customerOpt.get();

        // Latest bill
        Optional<ElectricityBill> latestBillOpt = billRepo
                .findTopByAccountNumberOrderByBillYearDescBillMonthDesc(accountNumber);

        // Bill history up to 6
        List<CustomerContext.BillSummary> billSummaries = billRepo
                .findByAccountNumberOrderByBillYearDescBillMonthDesc(accountNumber)
                .stream()
                .limit(6)
                .map(b -> CustomerContext.BillSummary.builder()
                        .month(b.getBillMonth())
                        .year(b.getBillYear() != null ? b.getBillYear().toString() : "N/A")
                        .units(b.getUnitsConsumed() != null ? b.getUnitsConsumed().toString() : "0")
                        .amount(b.getTotalAmount() != null ? b.getTotalAmount().toString() : "0")
                        .status(b.getBillStatus() != null ? b.getBillStatus() : "UNKNOWN")
                        .dueDate(b.getDueDate() != null ? b.getDueDate().toString() : "N/A")
                        .build())
                .collect(Collectors.toList());

        // Active complaints (OPEN + IN_PROGRESS)
        List<CustomerContext.ComplaintSummary> complaintSummaries = complaintRepo
                .findByAccountNumberOrderByCreatedAtDesc(accountNumber)
                .stream()
                .filter(c -> "OPEN".equals(c.getStatus()) || "IN_PROGRESS".equals(c.getStatus()))
                .limit(5)
                .map(c -> CustomerContext.ComplaintSummary.builder()
                        .complaintNumber(c.getComplaintNumber())
                        .type(c.getComplaintType())
                        .status(c.getStatus())
                        .createdAt(c.getCreatedAt() != null ? c.getCreatedAt().toLocalDate().toString() : "N/A")
                        .description(c.getDescription())
                        .build())
                .collect(Collectors.toList());

        // Recent payments up to 5
        List<CustomerContext.PaymentSummary> paymentSummaries = paymentRepo
                .findByAccountNumberOrderByPaymentDateDesc(accountNumber)
                .stream()
                .limit(5)
                .map(p -> CustomerContext.PaymentSummary.builder()
                        .amount(p.getAmount() != null ? p.getAmount().toString() : "0")
                        .mode(p.getPaymentMode())
                        .transactionId(p.getTransactionId())
                        .date(p.getPaymentDate() != null ? p.getPaymentDate().toLocalDate().toString() : "N/A")
                        .status(p.getStatus())
                        .build())
                .collect(Collectors.toList());

        // Build context
        CustomerContext.CustomerContextBuilder builder = CustomerContext.builder()
                .accountNumber(customer.getAccountNumber())
                .consumerName(customer.getConsumerName())
                .mobileNumber(customer.getMobileNumber())
                .address(customer.getAddress())
                .meterNumber(customer.getMeterNumber())
                .connectionType(customer.getConnectionType())
                .loadKw(customer.getLoadKw() != null ? customer.getLoadKw().toString() : "N/A")
                .connectionStatus(customer.getConnectionStatus())
                .recentBills(billSummaries)
                .activeComplaints(complaintSummaries)
                .recentPayments(paymentSummaries);

        // Latest bill fields
        if (latestBillOpt.isPresent()) {
            ElectricityBill lb = latestBillOpt.get();
            builder.latestBillMonth(lb.getBillMonth() != null ? lb.getBillMonth() : "N/A")
                   .latestBillYear(lb.getBillYear() != null ? lb.getBillYear().toString() : "N/A")
                   .latestUnitsConsumed(lb.getUnitsConsumed() != null ? lb.getUnitsConsumed().toString() : "0")
                   .latestFixedCharge(lb.getFixedCharge() != null ? lb.getFixedCharge().toString() : "0")
                   .latestEnergyCharge(lb.getEnergyCharge() != null ? lb.getEnergyCharge().toString() : "0")
                   .latestSubsidy(lb.getSubsidy() != null ? lb.getSubsidy().toString() : "0")
                   .latestPenalty(lb.getPenalty() != null ? lb.getPenalty().toString() : "0")
                   .latestTotalAmount(lb.getTotalAmount() != null ? lb.getTotalAmount().toString() : "0")
                   .latestDueDate(lb.getDueDate() != null ? lb.getDueDate().toString() : "N/A")
                   .latestBillStatus(lb.getBillStatus() != null ? lb.getBillStatus() : "UNKNOWN");
        } else {
            builder.latestBillMonth("N/A")
                   .latestBillYear("N/A")
                   .latestUnitsConsumed("0")
                   .latestFixedCharge("0")
                   .latestEnergyCharge("0")
                   .latestSubsidy("0")
                   .latestPenalty("0")
                   .latestTotalAmount("0")
                   .latestDueDate("N/A")
                   .latestBillStatus("NO BILLS FOUND");
        }

        return Optional.of(builder.build());
    }
}
