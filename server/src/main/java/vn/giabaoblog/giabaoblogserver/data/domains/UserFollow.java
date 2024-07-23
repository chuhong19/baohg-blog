package vn.giabaoblog.giabaoblogserver.data.domains;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_follow")
public class UserFollow extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[id]")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "follow_id", nullable = false)
    private Long followId;
}
