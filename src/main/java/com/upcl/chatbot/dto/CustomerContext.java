package com.upcl.chatbot.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class CustomerContext {

    private String accountNumber;
    private String consumerName;
    private String mobileNumber;
    private String address;
    private String meterNumber;
    private String connectionType;
    private String loadKw;
    private String connectionStatus;

    private String latestBillMonth;
    private String latestBillYear;
    private String latestUnitsConsumed;
    private String latestFixedCharge;
    private String latestEnergyCharge;
    private String latestSubsidy;
    private String latestPenalty;
    private String latestTotalAmount;
    private String latestDueDate;
    private String latestBillStatus;

    private List<BillSummary> recentBills;
    private List<ComplaintSummary> activeComplaints;
    private List<PaymentSummary> recentPayments;

    @Data
    @Builder
    public static class BillSummary {
        private String month;
        private String year;
        private String units;
        private String amount;
        private String status;
        private String dueDate;
    }

    @Data
    @Builder
    public static class ComplaintSummary {
        private String complaintNumber;
        private String type;
        private String status;
        private String createdAt;
        private String description;
    }

    @Data
    @Builder
    public static class PaymentSummary {
        private String amount;
        private String mode;
        private String transactionId;
        private String date;
        private String status;
    }

    public String toPromptBlock() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== CONSUMER PROFILE ===\n");
        sb.append("Account No      : ").append(accountNumber).append("\n");
        sb.append("Name            : ").append(consumerName).append("\n");
        sb.append("Mobile          : ").append(mobileNumber).append("\n");
        sb.append("Address         : ").append(address).append("\n");
        sb.append("Meter No        : ").append(meterNumber).append("\n");
        sb.append("Connection Type : ").append(connectionType).append("\n");
        sb.append("Load            : ").append(loadKw).append(" kW\n");
        sb.append("Status          : ").append(connectionStatus).append("\n\n");

        sb.append("=== LATEST BILL ===\n");
        sb.append("Month           : ").append(latestBillMonth).append(" ").append(latestBillYear).append("\n");
        sb.append("Units Consumed  : ").append(latestUnitsConsumed).append("\n");
        sb.append("Fixed Charge    : Rs.").append(latestFixedCharge).append("\n");
        sb.append("Energy Charge   : Rs.").append(latestEnergyCharge).append("\n");
        sb.append("Subsidy         : Rs.").append(latestSubsidy).append("\n");
        sb.append("Penalty         : Rs.").append(latestPenalty).append("\n");
        sb.append("Total Amount    : Rs.").append(latestTotalAmount).append("\n");
        sb.append("Due Date        : ").append(latestDueDate).append("\n");
        sb.append("Bill Status     : ").append(latestBillStatus).append("\n\n");

        if (recentBills != null && !recentBills.isEmpty()) {
            sb.append("=== BILL HISTORY ===\n");
            for (BillSummary b : recentBills) {
                sb.append("  * ").append(b.getMonth()).append(" ").append(b.getYear())
                  .append(" | Units: ").append(b.getUnits())
                  .append(" | Rs.").append(b.getAmount())
                  .append(" | ").append(b.getStatus())
                  .append(" | Due: ").append(b.getDueDate()).append("\n");
            }
            sb.append("\n");
        }

        if (activeComplaints != null && !activeComplaints.isEmpty()) {
            sb.append("=== ACTIVE COMPLAINTS ===\n");
            for (ComplaintSummary c : activeComplaints) {
                sb.append("  * [").append(c.getComplaintNumber()).append("] ")
                  .append(c.getType()).append(" | ").append(c.getStatus())
                  .append(" | Filed: ").append(c.getCreatedAt()).append("\n");
                sb.append("    -> ").append(c.getDescription()).append("\n");
            }
            sb.append("\n");
        } else {
            sb.append("=== ACTIVE COMPLAINTS ===\nNone\n\n");
        }

        if (recentPayments != null && !recentPayments.isEmpty()) {
            sb.append("=== RECENT PAYMENTS ===\n");
            for (PaymentSummary p : recentPayments) {
                sb.append("  * Rs.").append(p.getAmount())
                  .append(" | ").append(p.getMode())
                  .append(" | TXN: ").append(p.getTransactionId())
                  .append(" | ").append(p.getStatus())
                  .append(" | ").append(p.getDate()).append("\n");
            }
        }

        return sb.toString();
    }
}
