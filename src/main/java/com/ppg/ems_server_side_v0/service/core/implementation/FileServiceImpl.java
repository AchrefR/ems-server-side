package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Department;
import com.ppg.ems_server_side_v0.domain.Document;
import com.ppg.ems_server_side_v0.domain.User;
import com.ppg.ems_server_side_v0.dto.DocumentResponse;
import com.ppg.ems_server_side_v0.dto.FileFilterRequest;
import com.ppg.ems_server_side_v0.dto.FileUploadRequest;
import com.ppg.ems_server_side_v0.dto.DepartmentResponse;
import com.ppg.ems_server_side_v0.repository.DepartmentRepository;
import com.ppg.ems_server_side_v0.repository.DocumentRepository;
import com.ppg.ems_server_side_v0.repository.UserRepository;
import com.ppg.ems_server_side_v0.service.core.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileServiceImpl implements FileService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Value("${app.files.upload.dir:uploads/documents}")
    private String uploadDir;

    @Value("${app.files.max-size:10485760}") // 10MB default
    private long maxFileSize;

    private final Set<String> allowedFileTypes = Set.of(
        "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx",
        "txt", "csv", "jpg", "jpeg", "png", "gif", "bmp",
        "zip", "rar", "7z", "mp4", "avi", "mov", "mp3", "wav"
    );

    @Override
    public DocumentResponse uploadFile(MultipartFile file, FileUploadRequest request, String userId) {
        try {
            // Validate file
            validateFile(file);
            
            // Get user and department
            User user = userRepository.findById(userId).orElse(null);

            Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

            // Save file to disk
            String fileName = generateUniqueFileName(file.getOriginalFilename());
            String filePath = saveFileToDisk(file, fileName);

            // Create document entity
            Document document = Document.builder()
                .documentName(request.getFileName() != null ? request.getFileName() : file.getOriginalFilename())
                .documentType(getFileExtension(file.getOriginalFilename()))
                .ECMPath(filePath)
                .fileSize(formatFileSize(file.getSize()))
                .description(request.getDescription())
                .tags(request.getTags())
                .category(request.getCategory())
                .isPublic(request.isPublic())
                .status("ACTIVE")
                .uploadedBy(user)
                .department(department)
                .build();

            document = documentRepository.save(document);
            
            log.info("File uploaded successfully: {} by user: {}", fileName, userId);
            return mapToDocumentResponse(document);
            
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    public List<DocumentResponse> uploadMultipleFiles(List<MultipartFile> files, FileUploadRequest request, String userId) {
        return files.stream()
            .map(file -> uploadFile(file, request, userId))
            .collect(Collectors.toList());
    }

    @Override
    public Page<DocumentResponse> getAllDocuments(FileFilterRequest filterRequest) {
        try {
            Pageable pageable = createPageable(filterRequest);

            // Use simple query first, then add complexity
            String status = filterRequest.getStatus() != null ? filterRequest.getStatus() : "ACTIVE";
            Page<Document> documents;

            // If no filters, use simple findAll
            if (isSimpleRequest(filterRequest)) {
                documents = documentRepository.findByStatus(status, pageable);
            } else {
                // Use complex filtering
                documents = documentRepository.findDocumentsWithFilters(
                    filterRequest.getSearchQuery(),
                    filterRequest.getDepartmentIds(),
                    filterRequest.getFileTypes(),
                    filterRequest.getCategories(),
                    status,
                    filterRequest.getDateFrom(),
                    filterRequest.getDateTo(),
                    filterRequest.isPublicOnly(),
                    filterRequest.getUploadedBy(),
                    pageable
                );
            }

            return documents.map(this::mapToDocumentResponse);
        } catch (Exception e) {
            log.error("Error fetching documents: {}", e.getMessage(), e);
            // Return empty page on error
            return Page.empty();
        }
    }

    private boolean isSimpleRequest(FileFilterRequest filterRequest) {
        return filterRequest.getSearchQuery() == null &&
               (filterRequest.getDepartmentIds() == null || filterRequest.getDepartmentIds().isEmpty()) &&
               (filterRequest.getFileTypes() == null || filterRequest.getFileTypes().isEmpty()) &&
               (filterRequest.getCategories() == null || filterRequest.getCategories().isEmpty()) &&
               filterRequest.getDateFrom() == null &&
               filterRequest.getDateTo() == null &&
               !filterRequest.isPublicOnly() &&
               filterRequest.getUploadedBy() == null;
    }

    @Override
    public DocumentResponse getDocumentById(String documentId) {
        Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document not found"));
        return mapToDocumentResponse(document);
    }

    @Override
    public List<DocumentResponse> getDocumentsByDepartment(String departmentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Document> documents = documentRepository.findByDepartmentIdAndStatus(departmentId, "ACTIVE", pageable);
        return documents.getContent().stream()
            .map(this::mapToDocumentResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<DocumentResponse> getDocumentsByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Document> documents = documentRepository.findByUploadedByIdAndStatus(userId, "ACTIVE", pageable);
        return documents.getContent().stream()
            .map(this::mapToDocumentResponse)
            .collect(Collectors.toList());
    }

    @Override
    public List<DocumentResponse> getRecentDocuments(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createdAt").descending());
        Page<Document> documents = documentRepository.findRecentDocuments("ACTIVE", pageable);
        return documents.getContent().stream()
            .map(this::mapToDocumentResponse)
            .collect(Collectors.toList());
    }

    @Override
    public Resource downloadFile(String documentId) {
        try {
            Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
            
            Path filePath = Paths.get(document.getECMPath());
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error downloading file: " + e.getMessage());
        }
    }

    @Override
    public Resource previewFile(String documentId) {
        // For now, preview is same as download. Can be enhanced for specific preview logic
        return downloadFile(documentId);
    }

    @Override
    public String getFilePreviewUrl(String documentId) {
        return "/api/files/preview/" + documentId;
    }

    @Override
    public String getFileDownloadUrl(String documentId) {
        return "/api/files/download/" + documentId;
    }

    @Override
    public DocumentResponse updateDocument(String documentId, FileUploadRequest request) {
        Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document not found"));
        
        // Update fields
        if (request.getFileName() != null) {
            document.setDocumentName(request.getFileName());
        }
        if (request.getDescription() != null) {
            document.setDescription(request.getDescription());
        }
        if (request.getTags() != null) {
            document.setTags(request.getTags());
        }
        if (request.getCategory() != null) {
            document.setCategory(request.getCategory());
        }
        document.setPublic(request.isPublic());
        
        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
            document.setDepartment(department);
        }
        
        document = documentRepository.save(document);
        return mapToDocumentResponse(document);
    }

    @Override
    public void deleteDocument(String documentId) {
        Document document = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document not found"));
        
        // Soft delete
        document.setStatus("DELETED");
        documentRepository.save(document);
        
        log.info("Document deleted: {}", documentId);
    }

    @Override
    public void deleteMultipleDocuments(List<String> documentIds) {
        documentIds.forEach(this::deleteDocument);
    }

    @Override
    public Page<DocumentResponse> searchDocuments(String searchQuery, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Document> documents = documentRepository.searchDocuments(searchQuery, "ACTIVE", pageable);
        return documents.map(this::mapToDocumentResponse);
    }

    @Override
    public Page<DocumentResponse> filterDocuments(FileFilterRequest filterRequest) {
        return getAllDocuments(filterRequest);
    }

    @Override
    public Map<String, Long> getDocumentStatsByDepartment() {
        List<Object[]> stats = documentRepository.getDocumentStatsByDepartment("ACTIVE");
        return stats.stream()
            .collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (Long) row[1]
            ));
    }

    @Override
    public Map<String, Long> getDocumentStatsByFileType() {
        List<Object[]> stats = documentRepository.getDocumentStatsByFileType("ACTIVE");
        return stats.stream()
            .collect(Collectors.toMap(
                row -> (String) row[0],
                row -> (Long) row[1]
            ));
    }

    @Override
    public Map<String, Long> getDocumentStatsByCategory() {
        // Implementation for category stats
        return new HashMap<>();
    }

    @Override
    public long getTotalDocumentsCount() {
        return documentRepository.count();
    }

    @Override
    public long getDocumentsCountByUser(String userId) {
        return documentRepository.countByUserAndStatus(userId, "ACTIVE");
    }

    @Override
    public boolean isFileTypeAllowed(String fileType) {
        return allowedFileTypes.contains(fileType.toLowerCase());
    }

    @Override
    public boolean isFileSizeAllowed(long fileSize) {
        return fileSize <= maxFileSize;
    }

    @Override
    public String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex > 0 ? fileName.substring(lastDotIndex + 1).toLowerCase() : "";
    }

    @Override
    public String generateUniqueFileName(String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String uuid = UUID.randomUUID().toString();
        return extension.isEmpty() ? uuid : uuid + "." + extension;
    }

    @Override
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findAll().stream()
            .map(dept -> DepartmentResponse.builder()
                .id(dept.getId())
                .name(dept.getDepartmentName())
                .type(dept.getDepartmentType())
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCategories() {
        return Arrays.asList("Document", "Image", "Video", "Audio", "Archive", "Spreadsheet", "Presentation", "Other");
    }

    @Override
    public List<String> getAllFileTypes() {
        return new ArrayList<>(allowedFileTypes);
    }

    // Private utility methods
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!isFileTypeAllowed(fileExtension)) {
            throw new RuntimeException("File type not allowed: " + fileExtension);
        }

        if (!isFileSizeAllowed(file.getSize())) {
            throw new RuntimeException("File size exceeds maximum allowed size");
        }
    }

    private String saveFileToDisk(MultipartFile file, String fileName) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save file
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024.0));
        return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
    }

    private Pageable createPageable(FileFilterRequest filterRequest) {
        Sort sort = Sort.by("createdAt").descending();

        if (filterRequest.getSortBy() != null) {
            Sort.Direction direction = "asc".equalsIgnoreCase(filterRequest.getSortDirection())
                ? Sort.Direction.ASC : Sort.Direction.DESC;
            sort = Sort.by(direction, filterRequest.getSortBy());
        }

        return PageRequest.of(
            filterRequest.getPage() > 0 ? filterRequest.getPage() : 0,
            filterRequest.getSize() > 0 ? filterRequest.getSize() : 10,
            sort
        );
    }

    private DocumentResponse mapToDocumentResponse(Document document) {
        return DocumentResponse.builder()
            .id(document.getId())
            .documentName(document.getDocumentName())
            .documentType(document.getDocumentType())
            .filePath(document.getECMPath())
            .fileSize(document.getFileSize())
            .departmentId(document.getDepartment() != null ? document.getDepartment().getId() : null)
            .departmentName(document.getDepartment() != null ? document.getDepartment().getDepartmentName() : null)
            .folderId(document.getFolder() != null ? document.getFolder().getId() : null)
            .folderName(document.getFolder() != null ? document.getFolder().getFolderName() : null)
            .uploadedBy(document.getUploadedBy() != null ?
                document.getUploadedBy().getPerson().getFirstName() + " " +
                document.getUploadedBy().getPerson().getLastName() : "Unknown")
            .description(document.getDescription())
            .tags(document.getTags())
            .category(document.getCategory())
            .isPublic(document.isPublic())
            .status(document.getStatus())
            .createdAt(document.getCreatedAt())
            .updatedAt(document.getUpdatedAt())
            .downloadUrl(getFileDownloadUrl(document.getId()))
            .previewUrl(getFilePreviewUrl(document.getId()))
            .canPreview(canPreviewFile(document.getDocumentType()))
            .build();
    }

    private boolean canPreviewFile(String fileType) {
        Set<String> previewableTypes = Set.of("pdf", "jpg", "jpeg", "png", "gif", "bmp", "txt");
        return previewableTypes.contains(fileType.toLowerCase());
    }

    @Override
    public DocumentResponse createSampleDocument(FileUploadRequest request) {
        try {
            log.info("Creating sample document with name: {}", request.getFileName());

            // Get department
            Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

            // Create sample document entity
            Document document = Document.builder()
                .documentName(request.getFileName())
                .documentType("pdf")
                .ECMPath("sample/path/" + request.getFileName())
                .fileSize("1.2 MB")
                .description(request.getDescription())
                .tags(request.getTags())
                .category(request.getCategory())
                .isPublic(request.isPublic())
                .status("ACTIVE")
                .uploadedBy(null) // No user for sample
                .department(department)
                .build();

            document = documentRepository.save(document);
            log.info("Sample document created with ID: {}", document.getId());

            return mapToDocumentResponse(document);
        } catch (Exception e) {
            log.error("Error creating sample document: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create sample document", e);
        }
    }
}
