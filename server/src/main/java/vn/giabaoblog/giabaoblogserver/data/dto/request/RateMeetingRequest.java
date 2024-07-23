package vn.giabaoblog.giabaoblogserver.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RateMeetingRequest {
    public Long meetingId;
    public Long rating;
    public String comment;
}
