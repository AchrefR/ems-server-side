package com.ppg.ems_server_side_v0.model.api.request;

public record DocumentDTO(

        String documentName,

        String documentType,

        String folderId,

        String departmentId

) {}
