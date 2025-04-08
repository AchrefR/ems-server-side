package com.ppg.ems_server_side_v0.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Folder extends BaseEntity{

    private String folderName;

    private String ECMPath;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "folder")
    private List<Document>  documents;
}
