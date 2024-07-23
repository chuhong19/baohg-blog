package vn.giabaoblog.giabaoblogserver.data.dto.mix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermissionDTO {
    private String permissionKey;
    private Long roleId;
}
