package com.ppg.ems_server_side_v0.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Document extends BaseEntity {

    private String documentName;

    private String documentType;

    private String ECMPath;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "document")
    private Contract contract;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "document")
    private Cv resume;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "document")
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "folderId", referencedColumnName = "id", nullable = false)
    private Folder folder;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "departmentId", referencedColumnName = "id", nullable = false)
    private Department department;


}
