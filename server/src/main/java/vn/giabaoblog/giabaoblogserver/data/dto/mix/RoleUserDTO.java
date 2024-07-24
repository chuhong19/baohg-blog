package vn.giabaoblog.giabaoblogserver.data.dto.mix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleUserDTO {
    private Long roleId;
    private Long userId;
}
