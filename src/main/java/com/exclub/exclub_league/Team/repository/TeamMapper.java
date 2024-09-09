package com.exclub.exclub_league.Team.repository;
import com.exclub.exclub_league.Team.dto.*;
import com.exclub.exclub_league.Team.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDTO toDto(Team team); // Team 엔티티 -> TeamDTO

    @Mapping(source = "name", target = "name")
    Team toEntity(TeamDTO teamDTO); // TeamDTO -> Team 엔티티

    TeamPerformanceDTO toPerformanceDto(TeamPerformance performance);

    TeamPerformance toPerformanceEntity(TeamPerformanceDTO performanceDTO);

    TeamAttributesDTO toAttributesDto(TeamAttributes attributes);

    TeamAttributes toAttributesEntity(TeamAttributesDTO attributesDTO);

    LocationDTO toLocationDto(Location location);

    Location toLocationEntity(LocationDTO locationDTO);

    // Stadium 관련 매핑 메서드 추가
    StadiumDTO toStadiumDto(Stadium stadium);

    Stadium toStadiumEntity(StadiumDTO stadiumDTO);
}