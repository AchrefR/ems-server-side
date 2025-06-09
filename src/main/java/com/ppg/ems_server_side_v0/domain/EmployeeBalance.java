package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.Year;

@Entity
@Table(name = "employee_balances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBalance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(nullable = false)
    private Year year;
    
    // Annual Leave
    private Integer annualLeaveEntitlement;
    private Integer annualLeaveUsed;
    private Integer annualLeaveCarriedOver;
    
    // Sick Leave
    private Integer sickLeaveEntitlement;
    private Integer sickLeaveUsed;
    
    // Personal Leave
    private Integer personalLeaveEntitlement;
    private Integer personalLeaveUsed;
    
    // Compensatory Leave
    private Integer compensatoryLeaveEarned;
    private Integer compensatoryLeaveUsed;
    
    // Other Leave Types
    private Integer maternityLeaveUsed;
    private Integer paternityLeaveUsed;
    private Integer bereavementLeaveUsed;
    private Integer emergencyLeaveUsed;
    private Integer studyLeaveUsed;
    private Integer unpaidLeaveUsed;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private String createdBy;
    private String updatedBy;
    
    // Computed fields
    public Integer getAnnualLeaveBalance() {
        return (annualLeaveEntitlement != null ? annualLeaveEntitlement : 0) +
               (annualLeaveCarriedOver != null ? annualLeaveCarriedOver : 0) -
               (annualLeaveUsed != null ? annualLeaveUsed : 0);
    }
    
    public Integer getSickLeaveBalance() {
        return (sickLeaveEntitlement != null ? sickLeaveEntitlement : 0) -
               (sickLeaveUsed != null ? sickLeaveUsed : 0);
    }
    
    public Integer getPersonalLeaveBalance() {
        return (personalLeaveEntitlement != null ? personalLeaveEntitlement : 0) -
               (personalLeaveUsed != null ? personalLeaveUsed : 0);
    }
    
    public Integer getCompensatoryLeaveBalance() {
        return (compensatoryLeaveEarned != null ? compensatoryLeaveEarned : 0) -
               (compensatoryLeaveUsed != null ? compensatoryLeaveUsed : 0);
    }
    
    public Integer getTotalLeaveUsed() {
        return (annualLeaveUsed != null ? annualLeaveUsed : 0) +
               (sickLeaveUsed != null ? sickLeaveUsed : 0) +
               (personalLeaveUsed != null ? personalLeaveUsed : 0) +
               (compensatoryLeaveUsed != null ? compensatoryLeaveUsed : 0) +
               (maternityLeaveUsed != null ? maternityLeaveUsed : 0) +
               (paternityLeaveUsed != null ? paternityLeaveUsed : 0) +
               (bereavementLeaveUsed != null ? bereavementLeaveUsed : 0) +
               (emergencyLeaveUsed != null ? emergencyLeaveUsed : 0) +
               (studyLeaveUsed != null ? studyLeaveUsed : 0) +
               (unpaidLeaveUsed != null ? unpaidLeaveUsed : 0);
    }
}
