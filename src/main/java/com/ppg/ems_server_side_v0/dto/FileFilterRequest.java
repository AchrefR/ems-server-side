package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileFilterRequest {
    private String searchQuery;
    private List<String> departmentIds;
    private List<String> fileTypes;
    private List<String> categories;
    private String status;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private String sortBy;
    private String sortDirection;
    private int page;
    private int size;
    private boolean publicOnly;
    private String uploadedBy;
}
