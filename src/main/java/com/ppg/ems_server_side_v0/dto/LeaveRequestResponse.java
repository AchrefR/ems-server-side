package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestResponse {
    
    private String id;
    private String employeeId;
    private String employeeName;
    private String approverId;
    private String approverName;
    
    private String leaveType;
    private String leaveTypeDisplayName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer daysRequested;
    
    private String reason;
    private String comments;
    private String status;
    private String statusDisplayName;
    private String approverComments;
    
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    
    // Emergency contact during leave
    private String emergencyContact;
    private String emergencyContactPhone;
    
    // Work handover
    private String workHandover;
    private String coveringEmployeeId;
    private String coveringEmployeeName;
    
    // Medical certificate
    private Boolean medicalCertificateRequired;
    private Boolean medicalCertificateProvided;
    private String medicalCertificatePath;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    
    // Computed fields
    private Boolean isApproved;
    private Boolean isPending;
    private Boolean isRejected;
    private Boolean isCancelled;
}
