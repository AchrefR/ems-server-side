package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.dto.DocumentResponse;
import com.ppg.ems_server_side_v0.dto.FileFilterRequest;
import com.ppg.ems_server_side_v0.dto.FileUploadRequest;
import com.ppg.ems_server_side_v0.dto.DepartmentResponse;
import com.ppg.ems_server_side_v0.service.core.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;

    // File upload endpoints
    @PostMapping("/upload")
    public ResponseEntity<DocumentResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("departmentId") String departmentId,
            @RequestParam(value = "fileName", required = false) String fileName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "isPublic", defaultValue = "false") boolean isPublic) {
        
        try {
            String userId = getCurrentUserId();
            
            FileUploadRequest request = FileUploadRequest.builder()
                .fileName(fileName)
                .departmentId(departmentId)
                .description(description)
                .tags(tags)
                .category(category)
                .isPublic(isPublic)
                .build();
            
            DocumentResponse response = fileService.uploadFile(file, request, userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload/multiple")
    public ResponseEntity<List<DocumentResponse>> uploadMultipleFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("departmentId") String departmentId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "isPublic", defaultValue = "false") boolean isPublic) {
        
        try {
            String userId = getCurrentUserId();
            
            FileUploadRequest request = FileUploadRequest.builder()
                .departmentId(departmentId)
                .description(description)
                .tags(tags)
                .category(category)
                .isPublic(isPublic)
                .build();
            
            List<DocumentResponse> responses = fileService.uploadMultipleFiles(files, request, userId);
            return ResponseEntity.ok(responses);
            
        } catch (Exception e) {
            log.error("Error uploading multiple files: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // File retrieval endpoints
    @GetMapping
    public ResponseEntity<Page<DocumentResponse>> getAllDocuments(
            @RequestParam(value = "searchQuery", required = false) String searchQuery,
            @RequestParam(value = "departmentIds", required = false) List<String> departmentIds,
            @RequestParam(value = "fileTypes", required = false) List<String> fileTypes,
            @RequestParam(value = "categories", required = false) List<String> categories,
            @RequestParam(value = "status", defaultValue = "ACTIVE") String status,
            @RequestParam(value = "publicOnly", defaultValue = "false") boolean publicOnly,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        try {
            FileFilterRequest filterRequest = FileFilterRequest.builder()
                .searchQuery(searchQuery)
                .departmentIds(departmentIds)
                .fileTypes(fileTypes)
                .categories(categories)
                .status(status)
                .publicOnly(publicOnly)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .page(page)
                .size(size)
                .build();
            
            Page<DocumentResponse> documents = fileService.getAllDocuments(filterRequest);
            return ResponseEntity.ok(documents);
            
        } catch (Exception e) {
            log.error("Error retrieving documents: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<DocumentResponse> getDocumentById(@PathVariable String documentId) {
        try {
            DocumentResponse document = fileService.getDocumentById(documentId);
            return ResponseEntity.ok(document);
        } catch (Exception e) {
            log.error("Error retrieving document: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByDepartment(
            @PathVariable String departmentId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        try {
            List<DocumentResponse> documents = fileService.getDocumentsByDepartment(departmentId, page, size);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Error retrieving documents by department: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByUser(
            @PathVariable String userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        try {
            List<DocumentResponse> documents = fileService.getDocumentsByUser(userId, page, size);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Error retrieving documents by user: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<DocumentResponse>> getRecentDocuments(
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        
        try {
            List<DocumentResponse> documents = fileService.getRecentDocuments(limit);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Error retrieving recent documents: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // File download and preview endpoints
    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String documentId) {
        try {
            Resource resource = fileService.downloadFile(documentId);
            DocumentResponse document = fileService.getDocumentById(documentId);
            
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + document.getDocumentName() + "\"")
                .body(resource);
                
        } catch (Exception e) {
            log.error("Error downloading file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/preview/{documentId}")
    public ResponseEntity<Resource> previewFile(@PathVariable String documentId) {
        try {
            Resource resource = fileService.previewFile(documentId);
            DocumentResponse document = fileService.getDocumentById(documentId);
            
            // Determine content type based on file extension
            MediaType contentType = getContentType(document.getDocumentType());
            
            return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "inline; filename=\"" + document.getDocumentName() + "\"")
                .body(resource);
                
        } catch (Exception e) {
            log.error("Error previewing file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // File management endpoints
    @PutMapping("/{documentId}")
    public ResponseEntity<DocumentResponse> updateDocument(
            @PathVariable String documentId,
            @RequestBody FileUploadRequest request) {
        
        try {
            DocumentResponse response = fileService.updateDocument(documentId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating document: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable String documentId) {
        try {
            fileService.deleteDocument(documentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting document: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/multiple")
    public ResponseEntity<Void> deleteMultipleDocuments(@RequestBody List<String> documentIds) {
        try {
            fileService.deleteMultipleDocuments(documentIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting multiple documents: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search endpoints
    @GetMapping("/search")
    public ResponseEntity<Page<DocumentResponse>> searchDocuments(
            @RequestParam("query") String searchQuery,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        
        try {
            Page<DocumentResponse> documents = fileService.searchDocuments(searchQuery, page, size);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            log.error("Error searching documents: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Statistics endpoints
    @GetMapping("/stats/department")
    public ResponseEntity<Map<String, Long>> getDocumentStatsByDepartment() {
        try {
            Map<String, Long> stats = fileService.getDocumentStatsByDepartment();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error retrieving department stats: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats/filetype")
    public ResponseEntity<Map<String, Long>> getDocumentStatsByFileType() {
        try {
            Map<String, Long> stats = fileService.getDocumentStatsByFileType();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error retrieving file type stats: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Utility endpoints
    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        try {
            log.info("Getting all departments...");
            List<DepartmentResponse> departments = fileService.getAllDepartments();
            log.info("Found {} departments", departments.size());
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            log.error("Error retrieving departments: {}", e.getMessage(), e);
            // Return fallback departments
            List<DepartmentResponse> fallbackDepartments = Arrays.asList(
                DepartmentResponse.builder().id("1").name("Engineering").type("Technical").build(),
                DepartmentResponse.builder().id("2").name("Human Resources").type("Administrative").build(),
                DepartmentResponse.builder().id("3").name("Finance").type("Financial").build(),
                DepartmentResponse.builder().id("4").name("Marketing").type("Commercial").build(),
                DepartmentResponse.builder().id("5").name("Sales").type("Commercial").build()
            );
            return ResponseEntity.ok(fallbackDepartments);
        }
    }

    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {
        log.info("Test endpoint called");
        return ResponseEntity.ok("File service is working!");
    }

    // Test endpoint to create sample document
    @PostMapping("/test/sample")
    public ResponseEntity<DocumentResponse> createSampleDocument() {
        try {
            log.info("Creating sample document...");

            // Create a sample document for testing
            FileUploadRequest request = FileUploadRequest.builder()
                .fileName("Sample Document.pdf")
                .departmentId("8b9c0fdb-a099-42d2-a238-85d12db12055") // Engineering department ID
                .description("This is a sample document for testing the file management system")
                .tags("sample, test, demo")
                .category("Document")
                .isPublic(true)
                .build();

            // Create a mock file for testing
            DocumentResponse response = fileService.createSampleDocument(request);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error creating sample document: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        try {
            List<String> categories = fileService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            log.error("Error retrieving categories: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/filetypes")
    public ResponseEntity<List<String>> getAllFileTypes() {
        try {
            List<String> fileTypes = fileService.getAllFileTypes();
            return ResponseEntity.ok(fileTypes);
        } catch (Exception e) {
            log.error("Error retrieving file types: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Private utility methods
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return "default-user"; // For demo purposes
        }
        return "default-user";
    }

    private MediaType getContentType(String fileExtension) {
        return switch (fileExtension.toLowerCase()) {
            case "pdf" -> MediaType.APPLICATION_PDF;
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "txt" -> MediaType.TEXT_PLAIN;
            case "json" -> MediaType.APPLICATION_JSON;
            case "xml" -> MediaType.APPLICATION_XML;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}
