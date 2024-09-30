package com.exclub.exclub_league.League.service;
import com.exclub.exclub_league.League.entity.RegionCoordinates;
import com.exclub.exclub_league.League.entity.TournamentMatch;
import com.exclub.exclub_league.League.repository.TournamentMatchRepository;
import com.exclub.exclub_league.Team.entity.Team;
import com.exclub.exclub_league.Team.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamMatchingService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentMatchRepository matchRepository;

    /**
     * Haversine 공식을 사용하여 두 위치 간의 거리를 계산하는 메소드
     */
    private double calculateDistance(RegionCoordinates loc1, RegionCoordinates loc2) {
        double earthRadius = 6371.0; // 지구 반지름 (단위: km)
        double lat1 = loc1.getLatitude().doubleValue();
        double lon1 = loc1.getLongitude().doubleValue();
        double lat2 = loc2.getLatitude().doubleValue();
        double lon2 = loc2.getLongitude().doubleValue();

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius * c;
    }

    /**
     * 1라운드 매칭을 생성하는 메소드
     */
    public List<TournamentMatch> createFirstRoundMatches(String city) {
        // 1. 특정 도시에서 리그 신청한 팀들을 가져온다.
        List<Team> leagueTeams = teamRepository.findAllByIsLeagueApplicantTrueAndLocation_City(city);

        // 팀 목록 출력
        System.out.println("Number of league teams in city " + city + ": " + leagueTeams.size());
        if (leagueTeams.isEmpty()) {
            System.out.println("No teams found for league in city: " + city);
            return Collections.emptyList();
        }

        // 2. 팀을 가까운 거리 순으로 정렬
        leagueTeams.sort((team1, team2) -> {
            RegionCoordinates loc1 = team1.getLocation().getRegionCoordinates();
            RegionCoordinates loc2 = team2.getLocation().getRegionCoordinates();
            return Double.compare(calculateDistance(loc1, loc2), 0);
        });

        // 3. 2팀씩 묶어서 1라운드 매칭을 생성
        List<TournamentMatch> matches = new ArrayList<>();
        for (int i = 0; i < leagueTeams.size() - 1; i += 2) {
            TournamentMatch match = new TournamentMatch();
            match.setTeam1(leagueTeams.get(i));
            match.setTeam2(leagueTeams.get(i + 1));
            match.setCity(city);
            match.setRoundNumber(1); // 1라운드
            match.setMatchStatus("PENDING"); // 경기 상태는 진행 전으로 설정
            match.setMatchDate(null); // 경기 날짜는 나중에 설정하도록 null로 설정

            matches.add(match);
            System.out.println("Match created between: " + leagueTeams.get(i).getName() + " and " + leagueTeams.get(i + 1).getName());
        }

        // 부전승 처리 (홀수 팀일 경우 마지막 팀 부전승)
        if (leagueTeams.size() % 2 != 0) {
            TournamentMatch byeMatch = new TournamentMatch();
            byeMatch.setTeam1(leagueTeams.get(leagueTeams.size() - 1));
            byeMatch.setTeam2(null); // 상대 팀 없음 (부전승)
            byeMatch.setCity(city);
            byeMatch.setRoundNumber(1); // 1라운드
            byeMatch.setMatchStatus("BYE"); // 부전승
            byeMatch.setWinnerTeam(leagueTeams.get(leagueTeams.size() - 1)); // 부전승 처리
            byeMatch.setMatchDate(null); // 경기 날짜는 나중에 설정하도록 null로 설정

            matches.add(byeMatch);
            System.out.println("Bye match created for: " + leagueTeams.get(leagueTeams.size() - 1).getName());
        }

        // 매칭 정보 DB에 저장
        try {
            matchRepository.saveAll(matches);
            System.out.println("All matches saved to the database.");
        } catch (Exception e) {
            System.err.println("Error saving matches to the database: " + e.getMessage());
            e.printStackTrace();
        }

        return matches; // 생성된 매칭 리스트 반환
    }

    /**
     * 매칭 날짜를 업데이트하는 메소드
     */
    public void updateMatchDate(Long matchId, LocalDateTime matchDate) {
        // 1. 매칭 정보 가져오기
        TournamentMatch match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid match ID"));

        // 2. 매칭 날짜 업데이트
        match.setMatchDate(matchDate);

        // 3. 업데이트된 매칭 정보 DB에 저장
        matchRepository.save(match);
    }

    /**
     * 경기 결과를 업데이트하는 메소드
     */
    public void updateMatchResult(Long matchId, Long winnerTeamId) {
        // 1. 경기 정보 가져오기
        TournamentMatch match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid match ID"));

        // 2. 경기 결과 업데이트
        Team winnerTeam = teamRepository.findById(winnerTeamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team ID"));
        match.setWinnerTeam(winnerTeam);
        match.setMatchStatus("COMPLETED");

        // 3. 경기 정보 DB에 저장
        matchRepository.save(match);
    }

    /**
     * 다음 라운드 팀 매칭을 위한 승리 팀 리스트를 반환하는 메소드
     */
    public List<Team> getWinnersForNextRound(int roundNumber, String city) {
        // 1. 이전 라운드에서 승리한 팀들 가져오기
        List<TournamentMatch> completedMatches = matchRepository.findByRoundNumberAndCityAndMatchStatus(roundNumber, city, "COMPLETED");

        // 2. 승리 팀 리스트 추출
        List<Team> winners = completedMatches.stream()
                .map(TournamentMatch::getWinnerTeam)
                .collect(Collectors.toList());

        return winners; // 다음 라운드로 진출하는 팀 리스트 반환
    }

    /**
     * 다음 라운드 매칭을 생성하는 메소드
     */
    public List<TournamentMatch> createNextRoundMatches(int roundNumber, String city) {
        // 1. 다음 라운드로 진출한 팀들 가져오기
        List<Team> winners = getWinnersForNextRound(roundNumber - 1, city);

        // 2. 승리 팀들로 다음 라운드 매칭 생성
        List<TournamentMatch> nextRoundMatches = new ArrayList<>();
        for (int i = 0; i < winners.size() - 1; i += 2) {
            TournamentMatch match = new TournamentMatch();
            match.setTeam1(winners.get(i));
            match.setTeam2(winners.get(i + 1));
            match.setCity(city);
            match.setRoundNumber(roundNumber);
            match.setMatchStatus("PENDING"); // 경기 상태는 진행 전으로 설정
            match.setMatchDate(LocalDateTime.now().plusDays(7)); // 7일 후 경기 예정

            nextRoundMatches.add(match);
            matchRepository.save(match); // 매칭 정보 DB에 저장
        }

        // 부전승 처리 (홀수 팀일 경우 마지막 팀 부전승)
        if (winners.size() % 2 != 0) {
            TournamentMatch byeMatch = new TournamentMatch();
            byeMatch.setTeam1(winners.get(winners.size() - 1));
            byeMatch.setTeam2(null); // 상대 팀 없음 (부전승)
            byeMatch.setCity(city);
            byeMatch.setRoundNumber(roundNumber);
            byeMatch.setMatchStatus("BYE"); // 부전승
            byeMatch.setWinnerTeam(winners.get(winners.size() - 1)); // 부전승 처리

            nextRoundMatches.add(byeMatch);
            matchRepository.save(byeMatch);
        }

        return nextRoundMatches; // 생성된 매칭 리스트 반환
    }
}