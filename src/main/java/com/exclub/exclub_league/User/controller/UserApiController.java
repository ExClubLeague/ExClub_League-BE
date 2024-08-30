package com.exclub.exclub_league.User.controller;
import com.exclub.exclub_league.User.dto.LoginRequestDTO;
import com.exclub.exclub_league.User.dto.LoginResponseDTO;
import com.exclub.exclub_league.User.dto.UserRequestDTO;
import com.exclub.exclub_league.User.entity.User;
import com.exclub.exclub_league.User.service.UserService;
import com.exclub.exclub_league.config.jwt.RefreshTokenService;
import com.exclub.exclub_league.config.jwt.TokenProvider;
import com.exclub.exclub_league.config.jwt.TokenService;
import com.exclub.exclub_league.config.oauth.OAuth2SuccessHandler;
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

@RequiredArgsConstructor
@RestController
public class UserApiController {
    private final UserService userService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;


    @PostMapping("/signUp")
    public String signup(@RequestBody UserRequestDTO request) {
        userService.save(request);
        return "signIn success";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());
        return "logout success";
    }

    @PostMapping("/signIn")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            // 인증이 성공하면 사용자 정보와 토큰을 생성합니다.
            String email = loginRequest.getEmail();
            User user = userService.findByEmail(email); // email 기반으로 User를 가져옵니다.

            // 액세스 토큰 생성
            String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));

            // 리프레시 토큰 생성 및 저장
            String refreshToken = tokenProvider.generateToken(user, Duration.ofDays(30));
            oAuth2SuccessHandler.saveRefreshToken(user.getId(), refreshToken);

            // 응답 DTO 생성
            LoginResponseDTO responseDTO = new LoginResponseDTO(accessToken, refreshToken);

            return ResponseEntity.ok(responseDTO);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            e.printStackTrace(); // 디버깅을 위한 로그 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}