package com.ppg.ems_server_side_v0.mapper;

import com.ppg.ems_server_side_v0.domain.Team;
import com.ppg.ems_server_side_v0.model.api.response.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TeamMapper {

    TeamResponse toTeamResponse(Team team) {
        return new TeamResponse(
                team.getId()
        );
    }

    List<TeamResponse> toTeamResponseList(List<Team> teams) {
        List<TeamResponse> teamResponses = new ArrayList<>();
        teams.forEach(team ->
                teamResponses.add(toTeamResponse(team))
        );
        return teamResponses;
    }

}
