package com.exclub.exclub_league.User.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.exclub.exclub_league.User.entity.Address;
import com.exclub.exclub_league.User.entity.User;
import com.exclub.exclub_league.User.respository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private Address address;

    @BeforeEach
    void setUp() {
        // 테스트에 사용할 Address 객체 설정
        address = new Address();
        address.setAddressId(1L);
        address.setCity("123 Main St");
        address.setRadius(2);
        address.setState("12345");

        // 테스트에 사용할 User 객체 설정
        user = new User();
        user.setId(7L); // 특정 ID로 설정
        user.setPassword("encodedPassword"); // 인코딩된 비밀번호
        user.setUserName("Test User");
        user.setEmail("test@example.com");
        user.setPhoneNumber("123-456-7890");
        user.setBirthDate("1990-01-01");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setAddress(address);
    }

    @Test
    void findByEmail_Success() {
        // Mock 설정: userRepository가 findByEmail을 호출할 때 Optional.of(user)를 반환하도록 설정
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // 메소드 호출 및 결과 확인
        User result = userService.findByEmail("test@example.com");
        assertEquals(user, result);
    }

    @Test
    void findByEmail_UserNotFound() {
        // Mock 설정: userRepository가 findByEmail을 호출할 때 Optional.empty()를 반환하도록 설정
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // 예외 발생 확인
        assertThrows(IllegalArgumentException.class, () -> {
            userService.findByEmail("nonexistent@example.com");
        });
    }
}