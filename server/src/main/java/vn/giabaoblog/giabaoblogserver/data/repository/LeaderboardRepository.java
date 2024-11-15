package vn.giabaoblog.giabaoblogserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.giabaoblog.giabaoblogserver.data.domains.ScoreRecord;

import java.util.Optional;

@Repository
public interface LeaderboardRepository extends JpaRepository<ScoreRecord, Long> {

    Optional<ScoreRecord> findByName(String scoreName);

}
