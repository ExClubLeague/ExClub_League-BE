package com.exclub.exclub_league.Team.controller;
import com.exclub.exclub_league.Team.dto.TeamDTO;
import com.exclub.exclub_league.Team.service.TeamService;
import com.exclub.exclub_league.config.jwt.TokenProvider;
import com.exclub.exclub_league.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "팀 API", description = "팀과 관련된 작업을 수행합니다.")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final TokenProvider tokenProvider;

    @GetMapping("/public/teams")
    @Operation(summary = "모든 팀 조회", description = "모든 팀의 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "팀 목록 조회 성공")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/public/teams/{id}")
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

    @PostMapping("/teams")
    @Operation(summary = "팀 생성", description = "새 팀을 생성합니다.")
    @ApiResponse(responseCode = "201", description = "팀 생성 성공")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO teamDTO) {
        TeamDTO createdTeam = teamService.createTeam(teamDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    @PutMapping("/teams/{id}")
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

    @DeleteMapping("/teams/{id}")
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

    @PostMapping("/teams/{teamId}/members")
    public ResponseEntity<String> addMemberToTeam(
            @PathVariable Long teamId,
            @RequestHeader("Authorization") String authorizationHeader) {

        try {
            // Authorization 헤더에서 "Bearer " 부분 제거하고 토큰만 추출
            String token = authorizationHeader.startsWith("Bearer ")
                    ? authorizationHeader.substring(7)
                    : authorizationHeader;

            // 토큰에서 사용자 ID 추출
            Long userId = tokenProvider.getUserId(token);

            // 팀에 사용자 추가
            teamService.addMemberToTeam(teamId, userId);

            return ResponseEntity.ok("팀에 사용자를 성공적으로 추가했습니다.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("사용자를 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
