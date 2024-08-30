package com.exclub.exclub_league.User.service;
import java.time.Duration;
import com.exclub.exclub_league.User.dto.LoginResponseDTO;
import com.exclub.exclub_league.User.dto.UserRequestDTO;
import com.exclub.exclub_league.User.entity.Address;
import com.exclub.exclub_league.User.entity.User;
import com.exclub.exclub_league.User.respository.AddressRepository;
import com.exclub.exclub_league.User.respository.UserRepository;
import com.exclub.exclub_league.config.jwt.TokenProvider;
import com.exclub.exclub_league.config.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    public Long save(UserRequestDTO dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // AddressRequestDTO로 Address 엔티티 생성
        Address address = Address.builder()
                .state(dto.getAddress().getState())
                .city(dto.getAddress().getCity())
                .radius(dto.getAddress().getRadius())
                .build();

        addressRepository.save(address);

        // UserRequestDTO로 User 엔티티 생성
        User user = User.builder()
                .userName(dto.getUserName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .birthDate(dto.getBirthDate())
                .password(encoder.encode(dto.getPassword()))
                .address(address)  // 새로운 Address 연결
                .build();

        return userRepository.save(user).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

}
