package vn.giabaoblog.giabaoblogserver.data.dto.mix;

import lombok.Data;
import vn.giabaoblog.giabaoblogserver.data.dto.shortName.UserDTO;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class UserListDTO implements Serializable {
    private ArrayList<UserDTO> userList;

    public UserListDTO() {
        userList = new ArrayList<>();
    }
}
