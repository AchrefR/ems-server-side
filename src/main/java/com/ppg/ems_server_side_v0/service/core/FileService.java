package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.dto.DocumentResponse;
import com.ppg.ems_server_side_v0.dto.FileFilterRequest;
import com.ppg.ems_server_side_v0.dto.FileUploadRequest;
import com.ppg.ems_server_side_v0.dto.DepartmentResponse;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface FileService {
    
    // File upload operations
    DocumentResponse uploadFile(MultipartFile file, FileUploadRequest request, String userId);
    List<DocumentResponse> uploadMultipleFiles(List<MultipartFile> files, FileUploadRequest request, String userId);
    
    // File retrieval operations
    Page<DocumentResponse> getAllDocuments(FileFilterRequest filterRequest);
    DocumentResponse getDocumentById(String documentId);
    List<DocumentResponse> getDocumentsByDepartment(String departmentId, int page, int size);
    List<DocumentResponse> getDocumentsByUser(String userId, int page, int size);
    List<DocumentResponse> getRecentDocuments(int limit);
    
    // File download and preview operations
    Resource downloadFile(String documentId);
    Resource previewFile(String documentId);
    String getFilePreviewUrl(String documentId);
    String getFileDownloadUrl(String documentId);
    
    // File management operations
    DocumentResponse updateDocument(String documentId, FileUploadRequest request);
    void deleteDocument(String documentId);
    void deleteMultipleDocuments(List<String> documentIds);
    
    // File search and filter operations
    Page<DocumentResponse> searchDocuments(String searchQuery, int page, int size);
    Page<DocumentResponse> filterDocuments(FileFilterRequest filterRequest);
    
    // File statistics and analytics
    Map<String, Long> getDocumentStatsByDepartment();
    Map<String, Long> getDocumentStatsByFileType();
    Map<String, Long> getDocumentStatsByCategory();
    long getTotalDocumentsCount();
    long getDocumentsCountByUser(String userId);
    
    // File validation and utilities
    boolean isFileTypeAllowed(String fileType);
    boolean isFileSizeAllowed(long fileSize);
    String getFileExtension(String fileName);
    String generateUniqueFileName(String originalFileName);
    
    // Department and folder operations
    List<DepartmentResponse> getAllDepartments();
    List<String> getAllCategories();
    List<String> getAllFileTypes();

    // Test operations
    DocumentResponse createSampleDocument(FileUploadRequest request);
}
