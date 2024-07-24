package vn.giabaoblog.giabaoblogserver.data.dto.shortName;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.giabaoblog.giabaoblogserver.data.domains.Meeting;
import vn.giabaoblog.giabaoblogserver.data.enums.MeetingStatus;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MeetingDTO {

    public Long id;
    public String meetingName;
    public String meetingDescription;
    public Long hostId;
    public String hostName;
    public Long maxParticipant;
    public Long countParticipant;
    public Date timeStart;
    public Double rating;
    public Long ratingCount;
    public MeetingStatus status;
    public MeetingDTO(Meeting meeting) {
        this.id = meeting.getId();
        this.meetingName = meeting.getMeetingName();
        this.meetingDescription = meeting.getMeetingDescription();
        this.hostId = meeting.getHostId();
        this.hostName = meeting.getHostName();
        this.maxParticipant = meeting.getMaxParticipant();
        this.countParticipant = meeting.getCountParticipant();
        this.timeStart = meeting.getTimeStart();
        this.rating = meeting.getRating();
        this.ratingCount = meeting.getRatingCount();
        this.status = meeting.getStatus();
    }
}
