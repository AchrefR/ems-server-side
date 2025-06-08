package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.PositionDTO;
import com.ppg.ems_server_side_v0.model.api.response.PositionResponse;
import com.ppg.ems_server_side_v0.service.core.PositionService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/positions")
@RequiredArgsConstructor

public class PositionController {

    private final PositionService positionService;

    @PostMapping
    public ResponseEntity<PositionResponse> createPosition(@RequestBody PositionDTO positionDTO) {
        return new ResponseEntity<>(positionService.addPosition(positionDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PositionResponse> updatePosition(
            @RequestBody PositionDTO positionDTO,
            @PathVariable String id) {
        return ResponseEntity.ok(positionService.updatePosition(positionDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePosition(@PathVariable String id) {
        positionService.deletePosition(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PositionResponse> getPositionById(@PathVariable String id) {
        return ResponseEntity.ok(positionService.findPositionById(id));
    }

    @GetMapping
    public ResponseEntity<List<PositionResponse>> getAllPositions() {
        return ResponseEntity.ok(positionService.findAllPositions());
    }
}