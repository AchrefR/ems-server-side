package com.ppg.ems_server_side_v0.service.core;

import com.ppg.ems_server_side_v0.model.api.request.TeamDTO;
import com.ppg.ems_server_side_v0.model.api.response.TeamResponse;

import java.util.List;

public interface TeamService {

    TeamResponse addTeam(TeamDTO teamDTO);

    TeamResponse updateTeam(TeamDTO teamDTO, String id);

    void deleteTeam(String id);

    TeamResponse findTeamById(String id);

    List<TeamResponse> findAllTeams();
}