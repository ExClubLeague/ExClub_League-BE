package com.exclub.exclub_league.Team.service;
import com.exclub.exclub_league.Team.Repository.TeamRepository;
import com.exclub.exclub_league.Team.enetity.Team;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }
}