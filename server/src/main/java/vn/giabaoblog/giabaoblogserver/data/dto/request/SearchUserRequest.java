package vn.giabaoblog.giabaoblogserver.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserRequest {
    private Integer page;
    private Integer limit;
    private List<String> usernames;
    private List<String> emails;
    private List<String> roles;
    private List<String> permissions;
}
