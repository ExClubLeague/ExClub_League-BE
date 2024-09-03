package com.exclub.exclub_league.User.service;
import com.exclub.exclub_league.User.dto.UserRequestDTO;
import com.exclub.exclub_league.User.dto.UserResponseDTO;
import com.exclub.exclub_league.User.entity.Address;
import com.exclub.exclub_league.User.entity.User;
import com.exclub.exclub_league.User.respository.AddressRepository;
import com.exclub.exclub_league.User.respository.UserMapper;
import com.exclub.exclub_league.User.respository.UserRepository;
import com.exclub.exclub_league.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
