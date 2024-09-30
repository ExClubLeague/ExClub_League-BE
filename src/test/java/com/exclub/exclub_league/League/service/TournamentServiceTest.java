package com.exclub.exclub_league.League.service;
import com.exclub.exclub_league.League.entity.RegionCoordinates;
import com.exclub.exclub_league.League.entity.TournamentMatch;
import com.exclub.exclub_league.League.repository.RegionCoordinatesRepository;
import com.exclub.exclub_league.League.repository.TournamentMatchRepository;
import com.exclub.exclub_league.Team.entity.Location;
import com.exclub.exclub_league.Team.entity.Stadium;
import com.exclub.exclub_league.Team.entity.Team;
import com.exclub.exclub_league.Team.entity.TeamPerformance;
import com.exclub.exclub_league.Team.repository.LocationRepository;
import com.exclub.exclub_league.Team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import java.util.List;

@SpringBootTest
public class TournamentServiceTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private RegionCoordinatesRepository regionCoordinatesRepository;

    @Autowired
    private TeamMatchingService teamMatchingService;

    @Autowired
    private TournamentMatchRepository matchRepository;

    @Test
    @Rollback(false) // 테스트 후 데이터베이스에 저장
    public void testCreateFirstRoundMatches() {
        // Given: 테스트용 데이터를 설정
        String city = "서울";

//        // RegionCoordinates 객체 배열 생성
//        RegionCoordinates[] regions = {
//                new RegionCoordinates("서울특별시", BigDecimal.valueOf(37.5636), BigDecimal.valueOf(127.0426), "성동구"),
//                new RegionCoordinates("서울특별시", BigDecimal.valueOf(37.5383), BigDecimal.valueOf(127.0825), "광진구"),
//                new RegionCoordinates("서울특별시", BigDecimal.valueOf(37.5702), BigDecimal.valueOf(127.0202), "동대문구"),
//                new RegionCoordinates("서울특별시", BigDecimal.valueOf(37.6067), BigDecimal.valueOf(127.0898), "중랑구"),
//                new RegionCoordinates("서울특별시", BigDecimal.valueOf(37.6092), BigDecimal.valueOf(127.0202), "성북구"),
//                new RegionCoordinates("서울특별시", BigDecimal.valueOf(37.6395), BigDecimal.valueOf(127.0256), "강북구"),
//                new RegionCoordinates("서울특별시", BigDecimal.valueOf(37.6683), BigDecimal.valueOf(127.0317), "도봉구"),
//                new RegionCoordinates("서울특별시", BigDecimal.valueOf(37.6555), BigDecimal.valueOf(127.0560), "노원구"),
//                new RegionCoordinates("서울특별시", BigDecimal.valueOf(37.6100), BigDecimal.valueOf(126.9304), "은평구"),
//                new RegionCoordinates("서울특별시", BigDecimal.valueOf(37.5794), BigDecimal.valueOf(126.9644), "서대문구")
//        };
//
//        // RegionCoordinates 저장
//        for (RegionCoordinates region : regions) {
//            regionCoordinatesRepository.save(region);
//        }

        // 팀 생성 및 저장
        String[] districts = {
                "성동구", "광진구", "동대문구", "중랑구", "성북구",
                "강북구", "도봉구", "노원구", "은평구", "서대문구"
        };

        Team[] teams = new Team[districts.length];

        for (int i = 0; i < districts.length; i++) {
            teams[i] = new Team();
            teams[i].setName("Thunderbolts United " + (i + 1)); // 팀 이름 설정
            teams[i].setCode("TU2024-" + (i + 1)); // 팀 코드 설정
            teams[i].setLogoUrl("https://example.com/logos/thunderbolts-united-" + (i + 1) + ".png"); // 로고 URL 설정
            teams[i].setAgeGroup("36-45"); // 연령대 설정
            teams[i].setGender("Male"); // 성별 설정
            teams[i].setLeagueApplicant(true);
            teams[i].setSkillLevel("Beginner"); // 기술 수준 설정

            System.out.println("==============1===============");
            // 성능 설정
            TeamPerformance performance = new TeamPerformance();
            performance.setAttack(i+1);
            performance.setPass(i+1);
            performance.setDribble(i+1);
            performance.setPhysical(i+1);
            performance.setDefense(i+1);
            performance.setShoot(i+1);
            performance.setSpeed(i+1);
            performance.setStamina(i+2);
            System.out.println("==============2===============");
            // 성능 데이터 저장
            teams[i].setPerformance(performance);
            System.out.println("==============3===============");
            // 위치 설정
            Location location = createLocation(districts[i]); // districts[i]를 사용하여 Location 생성
            teams[i].setLocation(location);
            System.out.println("==============4===============");
            // 경기장 설정
            Stadium stadium = new Stadium();
            stadium.setName("Lightning Dome"); // 경기장 이름 설정
            stadium.setCity("Incheon"); // 경기장 도시 설정
            stadium.setDistrict("Namdong-gu"); // 경기장 구역 설정

            // 경기장 데이터 저장
            teams[i].setStadium(stadium);

            // 팀 저장
            teamRepository.save(teams[i]);
        }

// When: 매칭을 생성하는 메소드를 호출
        List<TournamentMatch> matches = teamMatchingService.createFirstRoundMatches("서울특별시");

// 매칭 결과를 데이터베이스에 저장 및 로그 출력
        for (TournamentMatch match : matches) {
            try {
                matchRepository.save(match);

                String matchInfo = String.format(
                        "Match saved: Team 1 - %s (Skill Level: %s, Region: %s) vs Team 2 - %s (Skill Level: %s, Region: %s)",
                        match.getTeam1().getName(),
                        match.getTeam1().getSkillLevel(),
                        match.getTeam1().getLocation().getRegion(),
                        match.getTeam2().getName(),
                        match.getTeam2().getSkillLevel(),
                        match.getTeam2().getLocation().getRegion()
                );

                System.out.println(matchInfo);
            } catch (Exception e) {
                System.err.println("Error saving match: " + e.getMessage()); // 매칭 저장 중 오류 발생 시 메시지 출력
                e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력하여 문제 확인
            }
        }
    }
// Location 생성 메소드
        private Location createLocation(String district) { // district를 파라미터로 받음
            Location location = new Location();
            location.setCity("서울특별시"); // 모든 팀의 도시를 서울로 설정
            location.setRegion(district); // 각각의 구역 설정

            // 미리 저장된 RegionCoordinates 조회
            RegionCoordinates regionCoordinates = regionCoordinatesRepository
                    .findByCityAndRegion("서울특별시", district)
                    .orElseThrow(() -> new RuntimeException("지역 좌표 정보가 없습니다."));

            // RegionCoordinates의 ID 값을 Location에 설정
            location.setRegionCoordinates(regionCoordinates);

            return location; // 생성된 Location 객체 반환
        }}
