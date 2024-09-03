package com.exclub.exclub_league.Team.repository;
import com.exclub.exclub_league.Team.dto.TeamAttributesDTO;
import com.exclub.exclub_league.Team.dto.TeamDTO;
import com.exclub.exclub_league.Team.dto.TeamPerformanceDTO;
import com.exclub.exclub_league.Team.entity.Team;
import com.exclub.exclub_league.Team.entity.TeamAttributes;
import com.exclub.exclub_league.Team.entity.TeamPerformance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamDTO toDto(Team team);

    Team toEntity(TeamDTO teamDTO);

    TeamPerformanceDTO toPerformanceDto(TeamPerformance performance);

    TeamPerformance toPerformanceEntity(TeamPerformanceDTO performanceDTO);

    TeamAttributesDTO toAttributesDto(TeamAttributes attributes);

    TeamAttributes toAttributesEntity(TeamAttributesDTO attributesDTO);
}