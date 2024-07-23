package vn.giabaoblog.giabaoblogserver.data.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "meeting_participant")
public class MeetingParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[id]")
    public Long id;

    @Column(name = "meeting_id", nullable = false)
    private Long meetingId;

    @Column(name = "host_id", nullable = false)
    private Long hostId;

    @Column(name = "participant_id", nullable = false)
    private Long participantId;
}
