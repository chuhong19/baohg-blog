package vn.giabaoblog.giabaoblogserver.data.dto.shortName;

import lombok.Data;
import vn.giabaoblog.giabaoblogserver.data.domains.Permission;
import vn.giabaoblog.giabaoblogserver.data.domains.Role;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.data.enums.Gender;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO implements Serializable {

    public UserDTO() {
        roles = new ArrayList<>();
        permissions = new ArrayList<>();
    }

    public UserDTO(User user) {
        if (user != null) {
            this.userId = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.firstname = user.getFirstname();
            this.lastname = user.getLastname();
            this.gender = user.getGender();
            this.enabled = user.isEnabled();

            roles = new ArrayList<>();
            permissions = new ArrayList<>();

            for (Role role : user.getRoles()) {
                roles.add(role.getRole());
                for (Permission p : role.getPermissions()) {
                    String key = p.getPermission();
                    if ((!permissions.contains(key)) && (p.isEnabled())) {
                        // add the permission only if enabled
                        permissions.add(key);
                    }
                }
            }
        }
    }

    private Long userId;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private Gender gender;
    private boolean enabled;

    private List<String> roles;
    private List<String> permissions;
}
