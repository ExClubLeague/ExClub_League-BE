package com.exclub.exclub_league.User.service;
import com.exclub.exclub_league.User.dto.UserRequestDTO;
import com.exclub.exclub_league.User.dto.UserResponseDTO;
import com.exclub.exclub_league.User.entity.Address;
import com.exclub.exclub_league.User.entity.User;
import com.exclub.exclub_league.User.respository.AddressRepository;
import com.exclub.exclub_league.User.respository.RoleRepository;
import com.exclub.exclub_league.User.respository.UserMapper;
import com.exclub.exclub_league.User.respository.UserRepository;
import com.exclub.exclub_league.exception.RoleNotFoundException;
import com.exclub.exclub_league.exception.UserNotFoundException;
import com.exclub.exclub_league.exception.UsernameAlreadyExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.exclub.exclub_league.User.entity.Role;

import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AddressRepository addressRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public Long save(UserRequestDTO dto) {
        UserMapper userMapper = UserMapper.INSTANCE;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 사용자가 입력한 사용자 이름이 이미 존재하는지 확인
        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        // AddressRequestDTO를 Address 엔티티로 변환
        Address address = userMapper.toAddress(dto.getAddress());
        addressRepository.save(address);  // 변환된 Address 엔티티를 데이터베이스에 저장

        // UserRequestDTO를 User 엔티티로 변환
        User user = userMapper.toUser(dto);
        user.setPassword(encoder.encode(dto.getPassword()));  // 비밀번호를 암호화
        user.setAddress(address);  // 변환된 Address 엔티티를 User 엔티티에 연결

        // 기본 역할(Role) 설정
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Role not found: ROLE_USER"));
        user.setRoles(Set.of(userRole));  // 기본 역할을 User 엔티티에 설정

        // 변환된 User 엔티티를 데이터베이스에 저장하고, 저장된 User의 ID를 반환
        return userRepository.save(user).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        log.info("이메일로 사용자 찾기 시도: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // UserMapper 객체를 직접 생성
        UserMapper userMapper = UserMapper.INSTANCE;
        return userMapper.toUserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        UserMapper userMapper = UserMapper.INSTANCE;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 기존 사용자 엔티티를 데이터베이스에서 조회
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // DTO를 엔티티로 변환하여 기존 사용자 엔티티 업데이트
        User userUpdates = userMapper.toUser(dto);

        // 기존 유저의 주소 정보를 업데이트
        Address updatedAddress = userMapper.toAddress(dto.getAddress());
        existingUser.setAddress(updatedAddress);

        // 기존 유저의 정보를 업데이트
        userMapper.updateUserFromDTO(dto, existingUser);

        // 비밀번호가 변경되었는지 확인하고 암호화
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existingUser.setPassword(encoder.encode(dto.getPassword()));
        }

        // 변경된 사용자 저장
        User updatedUser = userRepository.save(existingUser);

        // DTO로 변환하여 반환
        return userMapper.toUserResponseDTO(updatedUser);
    }
}
