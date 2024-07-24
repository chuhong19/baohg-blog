package vn.giabaoblog.giabaoblogserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.giabaoblog.giabaoblogserver.data.domains.MeetingParticipant;
import vn.giabaoblog.giabaoblogserver.data.domains.UserFollow;

import java.util.Optional;

@Repository
public interface MeetingParticipantRepository extends JpaRepository<MeetingParticipant, Long> {

    Optional<MeetingParticipant> findByMeetingIdAndParticipantId(Long meetingId, Long participantId);
}
