package com.ppg.ems_server_side_v0.controller;

import com.ppg.ems_server_side_v0.model.api.request.TeamDTO;
import com.ppg.ems_server_side_v0.model.api.response.TeamResponse;
import com.ppg.ems_server_side_v0.service.core.TeamService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TeamController {

    private final TeamService teamService;

    @PostMapping

    public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamDTO teamDTO) {
        return new ResponseEntity<>(
                teamService.addTeam(teamDTO),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")

    public ResponseEntity<TeamResponse> updateTeam(
            @PathVariable String id,
            @RequestBody TeamDTO teamDTO) {
        return ResponseEntity.ok(
                teamService.updateTeam(teamDTO, id)
        );
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleteTeam(@PathVariable String id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")

    public ResponseEntity<TeamResponse> getTeamById(@PathVariable String id) {
        return ResponseEntity.ok(
                teamService.findTeamById(id)
        );
    }

    @GetMapping

    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        return ResponseEntity.ok(
                teamService.findAllTeams()
        );
    }
}