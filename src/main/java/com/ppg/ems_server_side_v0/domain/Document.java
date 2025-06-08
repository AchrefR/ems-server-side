package com.ppg.ems_server_side_v0.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Document extends BaseEntity {

    private String documentName;

    private String documentType;

    private String ECMPath;

    @OneToOne(mappedBy = "document")
    private Contract contract;

    @OneToOne( mappedBy = "document")
    private Cv resume;

    @OneToOne( mappedBy = "document")
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "folderId", referencedColumnName = "id")
    private Folder folder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departmentId", referencedColumnName = "id")
    private Department department;


}
