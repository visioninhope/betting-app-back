package rs.ac.bg.fon.service;

import rs.ac.bg.fon.dtos.Fixture.FixtureDTO;
import rs.ac.bg.fon.entity.Fixture;
import rs.ac.bg.fon.utility.ApiResponse;

import java.time.LocalDateTime;
import java.util.List;


public interface FixtureService {

    Fixture save(Fixture fixture);
    Fixture getFixtureById(int fixtureId);

    List<Fixture> getNotStarted();

    boolean existsByDate(LocalDateTime date);

    List<Fixture> getNotStartedByLeague(Integer league);

    List<FixtureDTO> createFixtureDTOList(List<Fixture> fixtures);
    ApiResponse<?> getNotStartedByLeagueApiCall(Integer league);
    ApiResponse<?> getNotStartedApiCall();
}
