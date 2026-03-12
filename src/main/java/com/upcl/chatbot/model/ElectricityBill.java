package com.upcl.chatbot.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "electricity_bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElectricityBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "account_number", length = 50)
    private String accountNumber;

    @Column(name = "bill_month", length = 20)
    private String billMonth;

    @Column(name = "bill_year")
    private Integer billYear;

    @Column(name = "units_consumed")
    private Integer unitsConsumed;

    @Column(name = "previous_reading")
    private Integer previousReading;

    @Column(name = "current_reading")
    private Integer currentReading;

    @Column(name = "fixed_charge", precision = 10, scale = 2)
    private BigDecimal fixedCharge;

    @Column(name = "energy_charge", precision = 10, scale = 2)
    private BigDecimal energyCharge;

    @Column(name = "subsidy", precision = 10, scale = 2)
    private BigDecimal subsidy;

    @Column(name = "penalty", precision = 10, scale = 2)
    private BigDecimal penalty;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "bill_status", length = 20)
    private String billStatus;
}
