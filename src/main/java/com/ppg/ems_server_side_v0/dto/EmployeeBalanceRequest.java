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
public class EmployeeBalanceRequest {
    
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
}
