package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Document;
import com.ppg.ems_server_side_v0.model.api.response.DocumentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DocumentMapper {

    public DocumentResponse toDocumentResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getDocumentName(),
                document.getDocumentType(),
                document.getECMPath()
        );
    }

    public List<DocumentResponse> toDocumentReponseList(List<Document> documentList) {
        List<DocumentResponse> documentResponseList = new ArrayList<>();
        documentList.forEach(document -> {
            documentResponseList.add(toDocumentResponse(document));
        });
        return documentResponseList;
    }
}
