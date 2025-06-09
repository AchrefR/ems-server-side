package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestRequest {
    
    private String employeeId;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer daysRequested;
    private String reason;
    private String comments;
    private String emergencyContact;
    private String emergencyContactPhone;
    private String workHandover;
    private String coveringEmployeeId;
    private Boolean medicalCertificateRequired;
}
