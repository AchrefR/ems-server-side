package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.Team;
import com.ppg.ems_server_side_v0.mapper.TeamMapper;
import com.ppg.ems_server_side_v0.model.api.request.TeamDTO;
import com.ppg.ems_server_side_v0.model.api.response.TeamResponse;
import com.ppg.ems_server_side_v0.repository.TeamRepository;
import com.ppg.ems_server_side_v0.service.core.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    public TeamResponse addTeam(TeamDTO teamDTO) {
        Team team = Team.builder()
                .projects(new ArrayList<>())
                .employees(new ArrayList<>())
                .build();

        Team savedTeam = teamRepository.save(team);
        return teamMapper.toTeamResponse(savedTeam);
    }

    @Override
    public TeamResponse updateTeam(TeamDTO teamDTO, String id) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));
        
        // Comme le DTO est vide pour le moment, nous ne faisons que sauvegarder
        // l'équipe existante sans modifications
        Team updatedTeam = teamRepository.save(existingTeam);
        return teamMapper.toTeamResponse(updatedTeam);
    }

    @Override
    public void deleteTeam(String id) {
        if (!teamRepository.existsById(id)) {
            throw new RuntimeException("Équipe non trouvée");
        }
        teamRepository.deleteById(id);
    }

    @Override
    public TeamResponse findTeamById(String id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Équipe non trouvée"));
        return teamMapper.toTeamResponse(team);
    }

    @Override
    public List<TeamResponse> findAllTeams() {
        List<Team> teams = teamRepository.findAll();
        return teamMapper.toTeamResponseList(teams);
    }
}