package com.exclub.exclub_league.User.controller;
import com.exclub.exclub_league.User.dto.LoginRequestDTO;
import com.exclub.exclub_league.User.dto.LoginResponseDTO;
import com.exclub.exclub_league.User.dto.UserRequestDTO;
import com.exclub.exclub_league.User.dto.UserResponseDTO;
import com.exclub.exclub_league.User.entity.User;
import com.exclub.exclub_league.User.service.UserService;
import com.exclub.exclub_league.config.jwt.TokenProvider;
import com.exclub.exclub_league.config.oauth.OAuth2SuccessHandler;
import com.exclub.exclub_league.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import java.time.Duration;
import java.util.Collections;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "User API", description = "사용자 관리 API") // Swagger 그룹화
public class UserApiController {

    private final UserService userService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/public/users/signup")
    public ResponseEntity<String> signup(@RequestBody UserRequestDTO request) {
        userService.save(request);
        return ResponseEntity.ok("Sign up successful");
    }

    @Operation(summary = "로그인", description = "사용자 로그인 및 JWT 토큰 발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/public/users/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            String email = loginRequest.getEmail();
            User user = userService.findByEmail(email);

            String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));
            String refreshToken = tokenProvider.generateToken(user, Duration.ofDays(30));
            oAuth2SuccessHandler.saveRefreshToken(user.getId(), refreshToken);

            LoginResponseDTO responseDTO = new LoginResponseDTO(accessToken, refreshToken);
            return ResponseEntity.ok(responseDTO);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "로그아웃", description = "사용자 로그아웃 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content)
    })
    @GetMapping("/users/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "logout success";
    }

    @Operation(summary = "사용자 정보 조회", description = "ID를 통해 특정 사용자 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "사용자 없음", content = @Content)
    })
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        UserResponseDTO userResponseDTO = userService.getUserById(id);
        return ResponseEntity.ok(userResponseDTO);
    }

    @Operation(summary = "사용자 정보 수정", description = "ID를 통해 특정 사용자 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content),
            @ApiResponse(responseCode = "404", description = "사용자 없음", content = @Content)
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequestDTO,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Authorization 헤더에서 "Bearer " 부분 제거하고 토큰만 추출
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : authorizationHeader;

        try {
            // 토큰에서 사용자 ID 추출
            Long userIdFromToken = tokenProvider.getUserId(token);

            // 사용자 ID와 토큰의 사용자 ID 비교
            if (!userIdFromToken.equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Collections.singletonMap("error", "The token does not match the user ID."));
            }

            // 사용자 정보 업데이트
            UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            // 사용자 존재하지 않는 예외 처리
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            // 일반 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to update user. " + e.getMessage()));
        }
    }
}