package com.exclub.exclub_league.Team.service;
import com.exclub.exclub_league.Team.dto.TeamAttributesDTO;
import com.exclub.exclub_league.Team.dto.TeamPerformanceDTO;
import com.exclub.exclub_league.Team.entity.*;
import com.exclub.exclub_league.Team.repository.TeamMapper;
import com.exclub.exclub_league.Team.repository.TeamRepository;
import com.exclub.exclub_league.User.entity.User;
import com.exclub.exclub_league.User.service.UserService;
import com.exclub.exclub_league.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import com.exclub.exclub_league.Team.dto.TeamDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserService userService;

    @Transactional
    public TeamDTO createTeam(TeamDTO teamDTO) { // 팀 생성 메인 메소드
        User user = getAuthenticatedUser();
        Team team = convertToTeamEntity(teamDTO);
        team.setCreatedBy(user);

        Team savedTeam = saveTeam(team);
        return convertToTeamDTO(savedTeam);
    }

    private User getAuthenticatedUser() {  // 팀 생성 서브 메소드 - 1
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("사용자 인증 정보가 없습니다.");
        }
        String username = authentication.getName();
        return findUserByEmail(username);
    }

    private User findUserByEmail(String email) {  // 팀 생성 서브 메소드 - 2
        try {
            User user = userService.findByEmail(email);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
            }
            return user;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 정보를 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    private Team convertToTeamEntity(TeamDTO teamDTO) { // 팀 생성 서브 메소드 - 3
        try {
            return teamMapper.toEntity(teamDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "팀 정보를 변환하는 중 오류가 발생했습니다.", e);
        }
    }

    private Team saveTeam(Team team) { // 팀 생성 서브 메소드 - 4
        try {
            return teamRepository.save(team);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "팀 정보를 저장하는 중 오류가 발생했습니다.", e);
        }
    }

    private TeamDTO convertToTeamDTO(Team team) { // 팀 생성 서브 메소드 - 5
        try {
            return teamMapper.toDto(team);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "팀 정보를 변환하는 중 오류가 발생했습니다.", e);
        }
    }

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