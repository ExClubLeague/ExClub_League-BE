package com.exclub.exclub_league.Team.repository;
import com.exclub.exclub_league.Team.dto.TeamMemberDTO;
import com.exclub.exclub_league.User.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TeamMemberMapper {

    TeamMemberMapper INSTANCE = Mappers.getMapper(TeamMemberMapper.class);

    @Mapping(source = "username", target = "username") // source에서 username 매핑
    @Mapping(source = "age", target = "age")           // age 매핑
    @Mapping(source = "gender", target = "gender")     // gender 매핑
    TeamMemberDTO toDto(User user);
}
