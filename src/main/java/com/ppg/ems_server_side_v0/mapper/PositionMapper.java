package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Position;
import com.ppg.ems_server_side_v0.model.api.response.PositionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PositionMapper {

    PositionResponse toPositionResponse(Position position) {
        return new PositionResponse(
                position.getId(),
                position.getTitle()
        );
    }

    List<PositionResponse> toPositionResponseList(List<Position> positions) {
        List<PositionResponse> positionResponseList = new ArrayList<>();
        positions.forEach(position -> {
            positionResponseList.add(toPositionResponse(position));
        });
        return positionResponseList;

    }
}
