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
@Table(name= "meeting_rating")
public class MeetingRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[id]")
    public Long id;

    @Column(name = "meeting_id", nullable = false)
    public Long meetingId;

    @Column(name = "user_rating_id", nullable = false)
    public Long userId;

    @Column(name = "rate")
    public Long rating;

    @Column(name = "comment")
    public String comment;
}
