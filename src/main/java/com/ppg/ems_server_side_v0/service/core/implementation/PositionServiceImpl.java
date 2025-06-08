package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Position;
import com.ppg.ems_server_side_v0.mapper.PositionMapper;
import com.ppg.ems_server_side_v0.model.api.request.PositionDTO;
import com.ppg.ems_server_side_v0.model.api.response.PositionResponse;
import com.ppg.ems_server_side_v0.repository.PositionRepository;
import com.ppg.ems_server_side_v0.service.core.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    @Override
    public PositionResponse addPosition(PositionDTO positionDTO) {
        Position position = Position.builder()
                .title(positionDTO.positionTitle())
                .build();

        Position savedPosition = positionRepository.save(position);
        return positionMapper.toPositionResponse(savedPosition);
    }

    @Override
    public PositionResponse updatePosition(PositionDTO positionDTO, String id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));

        if (positionDTO.positionTitle() != null) {
            position.setTitle(positionDTO.positionTitle());
        }

        Position updatedPosition = positionRepository.save(position);
        return positionMapper.toPositionResponse(updatedPosition);
    }

    @Override
    public void deletePosition(String id) {
        if (!positionRepository.existsById(id)) {
            throw new RuntimeException("Position not found");
        }
        positionRepository.deleteById(id);
    }

    @Override
    public PositionResponse findPositionById(String id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        return positionMapper.toPositionResponse(position);
    }

    @Override
    public List<PositionResponse> findAllPositions() {
        List<Position> positions = positionRepository.findAll();
        return positionMapper.toPositionResponseList(positions);
    }
}