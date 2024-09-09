package com.exclub.exclub_league.Team.controller;
import com.exclub.exclub_league.Team.dto.TeamAttributesDTO;
import com.exclub.exclub_league.Team.dto.TeamDTO;
import com.exclub.exclub_league.Team.dto.TeamPerformanceDTO;
import com.exclub.exclub_league.Team.service.TeamService;
import com.exclub.exclub_league.User.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
@Tag(name = "팀 API", description = "팀과 관련된 작업을 수행합니다.")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "모든 팀 조회", description = "모든 팀의 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "팀 목록 조회 성공")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID로 팀 조회", description = "ID를 통해 특정 팀을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀 조회 성공"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음")
    })
    public ResponseEntity<TeamDTO> getTeamById(
            @Parameter(description = "팀 ID", example = "1") @PathVariable Long id) {
        TeamDTO team = teamService.getTeamById(id);
        return ResponseEntity.ok(team);
    }

    @PostMapping
    @Operation(summary = "팀 생성", description = "새 팀을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "팀 생성 성공")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {
        TeamDTO createdTeam = teamService.createTeam(teamDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    @PutMapping("/{id}")
    @Operation(summary = "팀 업데이트", description = "ID를 통해 팀 정보를 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음")
    })
    public ResponseEntity<TeamDTO> updateTeam(
            @Parameter(description = "팀 ID", example = "1") @PathVariable Long id,
            @RequestBody TeamDTO teamDTO) {
        TeamDTO updatedTeam = teamService.updateTeam(id, teamDTO);
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "팀 삭제", description = "ID를 통해 팀을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "팀 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음")
    })
    public ResponseEntity<Void> deleteTeam(
            @Parameter(description = "팀 ID", example = "1") @PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{teamId}/performance")
    @Operation(summary = "팀 성과 조회", description = "팀의 성과 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성과 조회 성공"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음")
    })
    public ResponseEntity<TeamPerformanceDTO> getTeamPerformance(
            @Parameter(description = "팀 ID", example = "1") @PathVariable Long teamId) {
        TeamPerformanceDTO performance = teamService.getTeamPerformance(teamId);
        return ResponseEntity.ok(performance);
    }

    @PutMapping("/{teamId}/performance")
    @Operation(summary = "팀 성과 업데이트", description = "팀의 성과 정보를 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성과 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음")
    })
    public ResponseEntity<TeamPerformanceDTO> updateTeamPerformance(
            @Parameter(description = "팀 ID", example = "1") @PathVariable Long teamId,
            @RequestBody TeamPerformanceDTO performanceDTO) {
        TeamPerformanceDTO updatedPerformance = teamService.updateTeamPerformance(teamId, performanceDTO);
        return ResponseEntity.ok(updatedPerformance);
    }

    @GetMapping("/{teamId}/attributes")
    @Operation(summary = "팀 속성 조회", description = "팀의 속성 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "속성 조회 성공"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음")
    })
    public ResponseEntity<TeamAttributesDTO> getTeamAttributes(
            @Parameter(description = "팀 ID", example = "1") @PathVariable Long teamId) {
        TeamAttributesDTO attributes = teamService.getTeamAttributes(teamId);
        return ResponseEntity.ok(attributes);
    }

    @PutMapping("/{teamId}/attributes")
    @Operation(summary = "팀 속성 업데이트", description = "팀의 속성 정보를 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "속성 업데이트 성공"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음")
    })
    public ResponseEntity<TeamAttributesDTO> updateTeamAttributes(
            @Parameter(description = "팀 ID", example = "1") @PathVariable Long teamId,
            @RequestBody TeamAttributesDTO attributesDTO) {
        TeamAttributesDTO updatedAttributes = teamService.updateTeamAttributes(teamId, attributesDTO);
        return ResponseEntity.ok(updatedAttributes);
    }
}
