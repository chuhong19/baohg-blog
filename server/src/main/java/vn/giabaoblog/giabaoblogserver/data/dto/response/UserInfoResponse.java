package vn.giabaoblog.giabaoblogserver.data.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.giabaoblog.giabaoblogserver.data.enums.Gender;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
    public String userId;
    public String username;
    public String email;
    public String firstname;
    public String lastname;
    public String gender;
}
