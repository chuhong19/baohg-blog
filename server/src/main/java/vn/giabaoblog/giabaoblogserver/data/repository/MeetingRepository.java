package vn.giabaoblog.giabaoblogserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.giabaoblog.giabaoblogserver.data.domains.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
