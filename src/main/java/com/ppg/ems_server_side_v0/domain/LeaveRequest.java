package com.ppg.ems_server_side_v0.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private Employee approver;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalDate endDate;
    
    @Column(nullable = false)
    private Integer daysRequested;
    
    @Column(columnDefinition = "TEXT")
    private String reason;
    
    @Column(columnDefinition = "TEXT")
    private String comments;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String approverComments;
    
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    
    // Emergency contact during leave
    private String emergencyContact;
    private String emergencyContactPhone;
    
    // Work handover
    @Column(columnDefinition = "TEXT")
    private String workHandover;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "covering_employee_id")
    private Employee coveringEmployee;
    
    // Medical certificate for sick leave
    private Boolean medicalCertificateRequired;
    private Boolean medicalCertificateProvided;
    private String medicalCertificatePath;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private String createdBy;
    private String updatedBy;
    
    // Computed fields
    public Boolean isApproved() {
        return status == LeaveStatus.APPROVED;
    }
    
    public Boolean isPending() {
        return status == LeaveStatus.PENDING;
    }
    
    public Boolean isRejected() {
        return status == LeaveStatus.REJECTED;
    }
    
    public Boolean isCancelled() {
        return status == LeaveStatus.CANCELLED;
    }
    
    // Enums
    public enum LeaveType {
        ANNUAL_LEAVE("Annual Leave"),
        SICK_LEAVE("Sick Leave"),
        PERSONAL_LEAVE("Personal Leave"),
        MATERNITY_LEAVE("Maternity Leave"),
        PATERNITY_LEAVE("Paternity Leave"),
        BEREAVEMENT_LEAVE("Bereavement Leave"),
        EMERGENCY_LEAVE("Emergency Leave"),
        STUDY_LEAVE("Study Leave"),
        UNPAID_LEAVE("Unpaid Leave"),
        COMPENSATORY_LEAVE("Compensatory Leave");
        
        private final String displayName;
        
        LeaveType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum LeaveStatus {
        PENDING("Pending Approval"),
        APPROVED("Approved"),
        REJECTED("Rejected"),
        CANCELLED("Cancelled"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed");
        
        private final String displayName;
        
        LeaveStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
