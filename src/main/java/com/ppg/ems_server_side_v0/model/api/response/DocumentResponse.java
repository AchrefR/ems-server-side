package com.ppg.ems_server_side_v0.model.api.response;

public record DocumentResponse(

        String documentId,

        String documentName,

        String documentType,

        String ECMPath

) {}
