package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Folder;
import com.ppg.ems_server_side_v0.model.api.response.FolderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FolderMapper {

    FolderResponse toFolderResponse(Folder folder) {
        return new FolderResponse(
                folder.getId(),
                folder.getFolderName(),
                folder.getECMPath()
        );
    }

    List<FolderResponse> toFolderResponseList(List<Folder> folders) {
        List<FolderResponse> foldersResponses = new ArrayList<>();
        folders.forEach(folder -> {
            foldersResponses.add(toFolderResponse(folder));
        });
        return foldersResponses;
    }
}
