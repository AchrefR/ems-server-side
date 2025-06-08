package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.PositionDTO;
import com.ppg.ems_server_side_v0.model.api.response.PositionResponse;

import java.util.List;

public interface PositionService {
    
    PositionResponse addPosition(PositionDTO positionDTO);
    
    PositionResponse updatePosition(PositionDTO positionDTO, String id);
    
    void deletePosition(String id);
    
    PositionResponse findPositionById(String id);
    
    List<PositionResponse> findAllPositions();
}