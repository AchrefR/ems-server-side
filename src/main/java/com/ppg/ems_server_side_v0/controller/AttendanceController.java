package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.AttendanceDTO;
import com.ppg.ems_server_side_v0.model.api.response.AttendanceResponse;
import com.ppg.ems_server_side_v0.service.core.AttendanceService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<AttendanceResponse> createAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        return new ResponseEntity<>(attendanceService.addAttendance(attendanceDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponse> updateAttendance(
            @RequestBody AttendanceDTO attendanceDTO,
            @PathVariable String id) {
        return ResponseEntity.ok(attendanceService.updateAttendance(attendanceDTO, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable String id) {
        attendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponse> getAttendanceById(@PathVariable String id) {
        return ResponseEntity.ok(attendanceService.findAttendanceById(id));
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> getAllAttendances() {
        return ResponseEntity.ok(attendanceService.findAllAttendances());
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceResponse>> getAttendancesByEmployeeId(
            @PathVariable String employeeId) {
        return ResponseEntity.ok(attendanceService.findAttendancesByEmployeeId(employeeId));
    }
}