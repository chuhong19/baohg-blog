package vn.giabaoblog.giabaoblogserver.data.domains;

import jakarta.persistence.*;
import lombok.*;
import vn.giabaoblog.giabaoblogserver.data.enums.MeetingStatus;

import java.util.Date;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[id]")
    public Long id;

    @Column(name= "meeting_name", nullable = false)
    public String meetingName;

    @Column(name= "meeting_description", nullable = false)
    public String meetingDescription;

    @Column(name= "host_id", nullable = false)
    public Long hostId;

    @JoinColumn(name = "host_name", nullable = false)
    public String hostName;

    @Column(name = "max_participant")
    public Long maxParticipant;

    @Column(name = "count_participant")
    public Long countParticipant;

    @Column(name = "time_start", nullable = false)
    public Date timeStart;

    @Column(name = "duration", nullable = false)
    public Long duration;

    @Column(name = "rating")
    public Double rating;

    @Column(name = "rating_count")
    public Long ratingCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    public MeetingStatus status;

}
