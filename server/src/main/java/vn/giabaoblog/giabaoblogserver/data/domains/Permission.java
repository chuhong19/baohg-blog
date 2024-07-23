package vn.giabaoblog.giabaoblogserver.data.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends Auditable {
    public Permission(Long id, String permission) {
        this.id = id;
        this.permission = permission;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @Column(name="id")
    private Long id;

    @Column(name="permission", nullable = true)
    private String permission;

    // enabled as default
    @Column(name="enabled")
    private boolean enabled = true;

    @Column(name="note")
    private String note;
}
