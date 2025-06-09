package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeBalanceResponse {
    
    private String id;
    private String employeeId;
    private String employeeName;
    private Year year;
    
    // Annual Leave
    private Integer annualLeaveEntitlement;
    private Integer annualLeaveUsed;
    private Integer annualLeaveCarriedOver;
    private Integer annualLeaveBalance;
    
    // Sick Leave
    private Integer sickLeaveEntitlement;
    private Integer sickLeaveUsed;
    private Integer sickLeaveBalance;
    
    // Personal Leave
    private Integer personalLeaveEntitlement;
    private Integer personalLeaveUsed;
    private Integer personalLeaveBalance;
    
    // Compensatory Leave
    private Integer compensatoryLeaveEarned;
    private Integer compensatoryLeaveUsed;
    private Integer compensatoryLeaveBalance;
    
    // Other Leave Types
    private Integer maternityLeaveUsed;
    private Integer paternityLeaveUsed;
    private Integer bereavementLeaveUsed;
    private Integer emergencyLeaveUsed;
    private Integer studyLeaveUsed;
    private Integer unpaidLeaveUsed;
    
    // Totals
    private Integer totalLeaveUsed;
    private Integer totalLeaveBalance;
}
