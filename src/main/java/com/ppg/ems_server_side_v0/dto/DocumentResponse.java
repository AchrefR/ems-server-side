package com.ppg.ems_server_side_v0.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {
    private String id;
    private String documentName;
    private String documentType;
    private String filePath;
    private String fileSize;
    private String departmentId;
    private String departmentName;
    private String folderId;
    private String folderName;
    private String uploadedBy;
    private String description;
    private String tags;
    private String category;
    private boolean isPublic;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String downloadUrl;
    private String previewUrl;
    private boolean canPreview;
}
