package com.ppg.ems_server_side_v0.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Document extends BaseEntity {

    @Column(nullable = false)
    private String documentName;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String ECMPath;

    @Column
    private String fileSize;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String tags;

    @Column
    private String category;

    @Column
    @Builder.Default
    private boolean isPublic = false;

    @Column
    @Builder.Default
    private String status = "ACTIVE";

    @Column
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploadedBy", referencedColumnName = "id")
    private User uploadedBy;

    @OneToOne(mappedBy = "document")
    private Contract contract;

    @OneToOne(mappedBy = "document")
    private Cv resume;

    @OneToOne(mappedBy = "document")
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "folderId", referencedColumnName = "id")
    private Folder folder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departmentId", referencedColumnName = "id")
    private Department department;

    @PrePersist
    protected void onCreate() {
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
