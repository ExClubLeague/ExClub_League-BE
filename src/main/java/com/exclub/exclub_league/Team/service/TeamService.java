package com.exclub.exclub_league.Team.service;
import com.exclub.exclub_league.Team.dto.TeamAttributesDTO;
import com.exclub.exclub_league.Team.dto.TeamPerformanceDTO;
import com.exclub.exclub_league.Team.entity.*;
import com.exclub.exclub_league.Team.repository.TeamMapper;
import com.exclub.exclub_league.Team.repository.TeamRepository;
import com.exclub.exclub_league.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import com.exclub.exclub_league.Team.dto.TeamDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    @Transactional(readOnly = true)
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeamDTO getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
        return teamMapper.toDto(team);
    }

    @Transactional
    public TeamDTO createTeam(TeamDTO teamDTO) {
        // Convert DTO to Entity
        Team team = teamMapper.toEntity(teamDTO);

        // Save Entity to database
        Team savedTeam = teamRepository.save(team);

        // Convert Entity back to DTO
        return teamMapper.toDto(savedTeam);
    }

    @Transactional
    public TeamDTO updateTeam(Long id, TeamDTO teamDTO) {
        // Check if the team exists
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

        // Map DTO to Entity
        Team updatedTeam = teamMapper.toEntity(teamDTO);

        // Keep the existing ID
        updatedTeam.setId(existingTeam.getId());

        // Save the updated entity
        Team savedTeam = teamRepository.save(updatedTeam);

        // Convert Entity back to DTO
        return teamMapper.toDto(savedTeam);
    }

    @Transactional
    public void deleteTeam(Long id) {
        // Check if the team exists
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

        // Delete the entity
        teamRepository.delete(team);
    }

    @Transactional(readOnly = true)
    public TeamPerformanceDTO getTeamPerformance(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));

        TeamPerformance performance = team.getPerformance();
        return teamMapper.toPerformanceDto(performance);
    }

    @Transactional
    public TeamPerformanceDTO updateTeamPerformance(Long teamId, TeamPerformanceDTO performanceDTO) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));

        TeamPerformance performance = teamMapper.toPerformanceEntity(performanceDTO);
        performance.setId(team.getPerformance().getId());

        team.setPerformance(performance);
        teamRepository.save(team);

        return teamMapper.toPerformanceDto(performance);
    }

    @Transactional(readOnly = true)
    public TeamAttributesDTO getTeamAttributes(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));

        TeamAttributes attributes = team.getAttributes();
        return teamMapper.toAttributesDto(attributes);
    }

    @Transactional
    public TeamAttributesDTO updateTeamAttributes(Long teamId, TeamAttributesDTO attributesDTO) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));

        TeamAttributes attributes = teamMapper.toAttributesEntity(attributesDTO);
        attributes.setId(team.getAttributes().getId());

        team.setAttributes(attributes);
        teamRepository.save(team);

        return teamMapper.toAttributesDto(attributes);
    }
}