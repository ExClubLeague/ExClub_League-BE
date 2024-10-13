package com.exclub.exclub_league.User.respository;
import com.exclub.exclub_league.User.dto.AddressRequestDTO;
import com.exclub.exclub_league.User.dto.AddressResponseDTO;
import com.exclub.exclub_league.User.dto.UserRequestDTO;
import com.exclub.exclub_league.User.dto.UserResponseDTO;
import com.exclub.exclub_league.User.entity.Address;
import com.exclub.exclub_league.User.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper { // 객체와 DTO를 매핑하기 위한 Mapper 인터페이스
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "username", target = "username")
    UserResponseDTO toUserResponseDTO(User user);

    @Mapping(source = "addressId", target = "addressId")
    AddressResponseDTO toAddressResponseDTO(Address address);

    @Mapping(target = "id", ignore = true)
    User toUser(UserRequestDTO dto);

    Address toAddress(AddressRequestDTO dto);

    // UserRequestDTO의 내용을 기존 User 엔티티에 적용
    void updateUserFromDTO(UserRequestDTO dto, @MappingTarget User user);
}