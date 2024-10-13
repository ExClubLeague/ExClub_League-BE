package com.exclub.exclub_league.Team.service;
import com.exclub.exclub_league.League.entity.RegionCoordinates;
import com.exclub.exclub_league.League.repository.RegionCoordinatesRepository;
import com.exclub.exclub_league.Team.dto.TeamAttributesDTO;
import com.exclub.exclub_league.Team.dto.TeamMemberDTO;
import com.exclub.exclub_league.Team.dto.TeamPerformanceDTO;
import com.exclub.exclub_league.Team.entity.*;
import com.exclub.exclub_league.Team.repository.TeamMapper;
import com.exclub.exclub_league.Team.repository.TeamMemberMapper;
import com.exclub.exclub_league.Team.repository.TeamRepository;
import com.exclub.exclub_league.User.entity.User;
import com.exclub.exclub_league.User.respository.UserRepository;
import com.exclub.exclub_league.User.service.UserService;
import com.exclub.exclub_league.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import com.exclub.exclub_league.Team.dto.TeamDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserService userService;
    private final RegionCoordinatesRepository regionCoordinatesRepository;
    private final UserRepository userRepository;
    private final TeamMemberMapper teamMemberMapper; // Mapper 주입
    @Transactional
    public TeamDTO createTeam(TeamDTO teamDTO) { // 팀 생성 메인 메소드
        User user = getAuthenticatedUser();

        // Location 객체 생성
        Location location = createLocation(teamDTO);

        // 팀 엔티티 변환 및 설정
        Team team = convertToTeamEntity(teamDTO);
        team.setLocation(location);
        team.setCreatedBy(user);

        Team savedTeam = saveTeam(team);

        // 팀을 생성한 사용자의 역할을 ROLE_CAPTAIN으로 업데이트
        userService.assignCaptainRole(user);

        return convertToTeamDTO(savedTeam);
    }

    private User getAuthenticatedUser() {  // 팀 생성 서브 메소드 - 1
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("사용자 인증 정보가 없습니다.");
            throw new AuthenticationCredentialsNotFoundException("사용자 인증 정보가 없습니다.");
        }
        String username = authentication.getName();
        return findUserByEmail(username);
    }

    private User findUserByEmail(String email) {
        try {
            User user = userService.findByEmail(email);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.");
            }
            return user;
        } catch (Exception e) {
            log.error("사용자 정보를 조회하는 중 오류가 발생했습니다. 에러 메시지: {}", e.getMessage());
            e.printStackTrace();  // 예외의 상세한 스택 트레이스를 콘솔에 출력
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 정보를 조회하는 중 오류가 발생했습니다.", e);
        }
    }

    private Location createLocation(TeamDTO teamDTO) { // Location 생성 메소드
        Location location = new Location();
        location.setCity(teamDTO.getLocation().getCity());
        location.setRegion(teamDTO.getLocation().getRegion());

        // 저장된 RegionCoordinates 조회
        RegionCoordinates regionCoordinates = regionCoordinatesRepository
                .findByCityAndRegion(teamDTO.getLocation().getCity(), teamDTO.getLocation().getRegion())
                .orElseThrow(() -> new RuntimeException("지역 좌표 정보가 없습니다."));

        // RegionCoordinates의 ID 값을 Location에 설정
        location.setRegionCoordinates(regionCoordinates); // RegionCoordinates 객체를 Location에 설정

        return location; // 생성된 Location 객체 반환
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

    @Transactional
    public void addMemberToTeam(Long teamId, Long userId) { // 팀 가입 메소드
        // 팀 정보 조회
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

        // 사용자 조회
        User user = userService.findById(userId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        // 이미 팀에 가입된 경우 예외 처리
        if (Boolean.TRUE.equals(user.getJoinedTeam())) {
            throw new RuntimeException("해당 사용자는 이미 팀에 가입되어 있습니다.");
        }

        // 팀에 사용자 추가
        team.getMembers().add(user);

        // 사용자의 joinedTeam 필드를 true로 설정
        user.setJoinedTeam(true);

        // 팀과 사용자 정보를 저장
        teamRepository.save(team);
        userRepository.save(user);
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

        // 팀원 정보를 가져옴
        List<User> teamMembers = team.getMembers().stream().collect(Collectors.toList());
        List<TeamMemberDTO> memberDTOs = teamMembers.stream()
                .map(teamMemberMapper::toDto)
                .collect(Collectors.toList());

        // TeamDTO에 팀원 정보 추가
        TeamDTO teamDTO = teamMapper.toDto(team);
        teamDTO.setMembers(memberDTOs);

        return teamDTO;
    }

    private TeamMemberDTO convertToTeamMemberDTO(User user) { // 팀에
        return teamMemberMapper.toDto(user); // Mapper를 사용하여 변환
    }

    @Transactional
    public TeamDTO updateTeam(Long id, TeamDTO teamDTO) {
        // 팀이 존재하는지 확인
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("팀을 찾을 수 없습니다. ID: " + id));

        // 인증된 사용자 정보 가져오기
        User authenticatedUser = getAuthenticatedUser();

        // 인증된 사용자가 팀 생성자인지 확인
        if (!existingTeam.getCreatedBy().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("이 팀을 수정할 권한이 없습니다.");
        }

        // 팀 DTO에서 null이 아닌 값만 기존 팀의 값을 업데이트
        if (teamDTO.getName() != null) {
            existingTeam.setName(teamDTO.getName());
        }
        if (teamDTO.getCode() != null) {
            existingTeam.setCode(teamDTO.getCode());
        }
        if (teamDTO.getLogoUrl() != null) {
            existingTeam.setLogoUrl(teamDTO.getLogoUrl());
        }
        if (teamDTO.getAgeGroup() != null) {
            existingTeam.setAgeGroup(teamDTO.getAgeGroup());
        }
        if (teamDTO.getGender() != null) {
            existingTeam.setGender(teamDTO.getGender());
        }
        if (teamDTO.getSkillLevel() != null) {
            existingTeam.setSkillLevel(teamDTO.getSkillLevel());
        }

        // Stadium, Location, Performance, Attributes 업데이트
        if (teamDTO.getStadium() != null) {
            existingTeam.setStadium(teamMapper.toStadiumEntity(teamDTO.getStadium()));
        }
        if (teamDTO.getLocation() != null) {
            existingTeam.setLocation(teamMapper.toLocationEntity(teamDTO.getLocation()));
        }
        if (teamDTO.getPerformance() != null) {
            existingTeam.setPerformance(teamMapper.toPerformanceEntity(teamDTO.getPerformance()));
        }
        if (teamDTO.getAttributes() != null) {
            existingTeam.setAttributes(teamMapper.toAttributesEntity(teamDTO.getAttributes()));
        }

        // 수정된 엔티티 저장
        Team savedTeam = teamRepository.save(existingTeam);

        // 엔티티를 DTO로 변환하여 반환
        return teamMapper.toDto(savedTeam);
    }

    @Transactional
    public void deleteTeam(Long id) {
        // 삭제할 팀을 조회
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));

        // 인증된 사용자 정보 가져오기
        User authenticatedUser = getAuthenticatedUser();

        // 인증된 사용자가 팀 생성자인지 확인
        if (!team.getCreatedBy().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("이 팀을 삭제할 권한이 없습니다.");
        }

        // 해당 팀을 삭제
        teamRepository.delete(team);
    }
//
//    @Transactional(readOnly = true)
//    public TeamPerformanceDTO getTeamPerformance(Long teamId) {
//        Team team = teamRepository.findById(teamId)
//                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));
//
//        TeamPerformance performance = team.getPerformance();
//        return teamMapper.toPerformanceDto(performance);
//    }
//
//    @Transactional
//    public TeamPerformanceDTO updateTeamPerformance(Long teamId, TeamPerformanceDTO performanceDTO) {
//        Team team = teamRepository.findById(teamId)
//                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));
//
//        TeamPerformance performance = teamMapper.toPerformanceEntity(performanceDTO);
//        performance.setId(team.getPerformance().getId());
//
//        team.setPerformance(performance);
//        teamRepository.save(team);
//
//        return teamMapper.toPerformanceDto(performance);
//    }
//
//    @Transactional(readOnly = true)
//    public TeamAttributesDTO getTeamAttributes(Long teamId) {
//        Team team = teamRepository.findById(teamId)
//                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));
//
//        TeamAttributes attributes = team.getAttributes();
//        return teamMapper.toAttributesDto(attributes);
//    }
//
//    @Transactional
//    public TeamAttributesDTO updateTeamAttributes(Long teamId, TeamAttributesDTO attributesDTO) {
//        Team team = teamRepository.findById(teamId)
//                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + teamId));
//
//        TeamAttributes attributes = teamMapper.toAttributesEntity(attributesDTO);
//        attributes.setId(team.getAttributes().getId());
//
//        team.setAttributes(attributes);
//        teamRepository.save(team);
//
//        return teamMapper.toAttributesDto(attributes);
//    }
}