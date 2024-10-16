package vn.giabaoblog.giabaoblogserver.data.domains;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="refresh_token")
public class RefreshToken extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "[user_id]", referencedColumnName = "[id]")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "[revoked]")
    public boolean revoked;

    @Column(nullable = false)
    private Instant expiryDate;


}
