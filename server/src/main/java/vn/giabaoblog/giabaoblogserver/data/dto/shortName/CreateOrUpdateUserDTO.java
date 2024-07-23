package vn.giabaoblog.giabaoblogserver.data.dto.shortName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.giabaoblog.giabaoblogserver.data.enums.Gender;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdateUserDTO implements Serializable {

    private Long userId;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private Gender gender;
    private String email;
    private boolean enabled;

}
