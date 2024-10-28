package com.exclub.exclub_league.Team.repository;
import com.exclub.exclub_league.Team.dto.*;
import com.exclub.exclub_league.Team.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDTO toDto(Team team); // Team 엔티티 -> TeamDTO

    @Mapping(source = "stadium", target = "stadium")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "performance", target = "performance")
    @Mapping(source = "attributes", target = "attributes")
    @Mapping(source = "name", target = "name")
    Team toEntity(TeamDTO teamDTO); // TeamDTO -> Team 엔티티

    TeamPerformanceDTO toPerformanceDto(TeamPerformance performance);

    TeamPerformance toPerformanceEntity(TeamPerformanceDTO performanceDTO);

    TeamAttributesDTO toAttributesDto(TeamAttributes attributes);

    TeamAttributes toAttributesEntity(TeamAttributesDTO attributesDTO);

    // TeamLocationDTO -> TeamLocation 매핑
    @Mapping(source = "city", target = "city")
    @Mapping(source = "region", target = "region")
    TeamLocation toTeamLocationEntity(LocationDTO locationDTO); // LocationDTO -> TeamLocation 매핑

    // TeamLocationDTO를 TeamLocation으로 매핑하는 메서드 추가
    TeamLocation toTeamLocationEntity(TeamLocationDTO teamLocationDTO);

    // TeamLocation -> TeamLocationDTO 매핑
    @Mapping(source = "city", target = "city")
    @Mapping(source = "region", target = "region")
    LocationDTO toLocationDto(TeamLocation teamLocation); // TeamLocation -> LocationDTO 매핑

    // Stadium 관련 매핑 메서드 추가
    StadiumDTO toStadiumDto(Stadium stadium);

    Stadium toStadiumEntity(StadiumDTO stadiumDTO);
}