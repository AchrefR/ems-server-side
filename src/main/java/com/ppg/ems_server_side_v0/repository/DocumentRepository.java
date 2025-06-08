package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {

    // Find documents by department
    Page<Document> findByDepartmentIdAndStatus(String departmentId, String status, Pageable pageable);

    // Find documents by multiple departments
    @Query("SELECT d FROM Document d WHERE d.department.id IN :departmentIds AND d.status = :status")
    Page<Document> findByDepartmentIdsAndStatus(@Param("departmentIds") List<String> departmentIds,
                                               @Param("status") String status, Pageable pageable);

    // Find documents by file type
    Page<Document> findByDocumentTypeInAndStatus(List<String> documentTypes, String status, Pageable pageable);

    // Find documents by category
    Page<Document> findByCategoryAndStatus(String category, String status, Pageable pageable);

    // Find public documents
    Page<Document> findByIsPublicTrueAndStatus(String status, Pageable pageable);

    // Find documents by uploaded user
    Page<Document> findByUploadedByIdAndStatus(String userId, String status, Pageable pageable);

    // Search documents by name or description
    @Query("SELECT d FROM Document d WHERE " +
           "(LOWER(d.documentName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
           "LOWER(d.tags) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) AND " +
           "d.status = :status")
    Page<Document> searchDocuments(@Param("searchQuery") String searchQuery,
                                 @Param("status") String status, Pageable pageable);

    // Complex search with multiple filters
    @Query("SELECT d FROM Document d WHERE " +
           "(:searchQuery IS NULL OR " +
           "LOWER(d.documentName) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
           "LOWER(d.description) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
           "LOWER(d.tags) LIKE LOWER(CONCAT('%', :searchQuery, '%'))) AND " +
           "(:departmentIds IS NULL OR d.department.id IN :departmentIds) AND " +
           "(:fileTypes IS NULL OR d.documentType IN :fileTypes) AND " +
           "(:categories IS NULL OR d.category IN :categories) AND " +
           "(:status IS NULL OR d.status = :status) AND " +
           "(:dateFrom IS NULL OR d.createdAt >= :dateFrom) AND " +
           "(:dateTo IS NULL OR d.createdAt <= :dateTo) AND " +
           "(:publicOnly = false OR d.isPublic = true) AND " +
           "(:uploadedBy IS NULL OR d.uploadedBy.id = :uploadedBy)")
    Page<Document> findDocumentsWithFilters(
            @Param("searchQuery") String searchQuery,
            @Param("departmentIds") List<String> departmentIds,
            @Param("fileTypes") List<String> fileTypes,
            @Param("categories") List<String> categories,
            @Param("status") String status,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo,
            @Param("publicOnly") boolean publicOnly,
            @Param("uploadedBy") String uploadedBy,
            Pageable pageable);

    // Simple find by status
    Page<Document> findByStatus(String status, Pageable pageable);

    // Find documents by folder
    Page<Document> findByFolderIdAndStatus(String folderId, String status, Pageable pageable);

    // Count documents by department
    @Query("SELECT COUNT(d) FROM Document d WHERE d.department.id = :departmentId AND d.status = :status")
    long countByDepartmentAndStatus(@Param("departmentId") String departmentId, @Param("status") String status);

    // Count documents by user
    @Query("SELECT COUNT(d) FROM Document d WHERE d.uploadedBy.id = :userId AND d.status = :status")
    long countByUserAndStatus(@Param("userId") String userId, @Param("status") String status);

    // Find recent documents
    @Query("SELECT d FROM Document d WHERE d.status = :status ORDER BY d.createdAt DESC")
    Page<Document> findRecentDocuments(@Param("status") String status, Pageable pageable);

    // Get document statistics
    @Query("SELECT d.department.departmentName as department, COUNT(d) as count " +
           "FROM Document d WHERE d.status = :status GROUP BY d.department.departmentName")
    List<Object[]> getDocumentStatsByDepartment(@Param("status") String status);

    @Query("SELECT d.documentType as fileType, COUNT(d) as count " +
           "FROM Document d WHERE d.status = :status GROUP BY d.documentType")
    List<Object[]> getDocumentStatsByFileType(@Param("status") String status);
}
