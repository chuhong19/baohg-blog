package vn.giabaoblog.giabaoblogserver.data.domains;

import jakarta.persistence.*;
import lombok.*;
import vn.giabaoblog.giabaoblogserver.data.enums.TokenType;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[id]")
    public Long id;

    @Column(name = "[token]", unique = true)
    public String token;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "[token_type]")
    public TokenType tokenType = TokenType.BEARER;

    @Column(name = "[revoked]")
    public boolean revoked;

    @Column(name = "[expired]")
    public boolean expired;

    @JoinColumn(name = "[user_id]", referencedColumnName = "[id]",
            nullable = true, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    public User user;
}
