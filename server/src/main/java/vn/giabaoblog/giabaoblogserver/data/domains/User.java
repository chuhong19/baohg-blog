package vn.giabaoblog.giabaoblogserver.data.domains;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.giabaoblog.giabaoblogserver.data.enums.Gender;

import java.util.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "users_unique_username", columnNames = {"username"}),
                @UniqueConstraint(name = "users_unique_email", columnNames = {"email"})
        }
)
public class User extends Auditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "[firstname]")
    private String firstname;

    @Column(name = "[lastname]")
    private String lastname;

    @Enumerated(EnumType.STRING)
    @Column(name = "[gender]")
    private Gender gender;

    @Builder.Default
    @Column(name = "[username]")
    private String username = null;

    @Builder.Default
    @Column(name = "[email]")
    private String email = null;

    @Builder.Default
    @Column(name = "[password]")
    private String password = null;

    @Builder.Default
    @Column(name = "[resetToken]")
    private String resetToken = null;

    @Builder.Default
    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @ManyToMany(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "users_special_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "special_permission_id"))
    private Set<Permission> special_permissions = new HashSet<>();

    @Builder.Default
    @Column(name = "[enabled]", nullable = false)
    private Boolean enabled = true;

    @Builder.Default
    @Column(name = "[account_non_expired]", nullable = false)
    private boolean accountNonExpired = true;

    @Builder.Default
    @Column(name = "[account_non_locked]", nullable = false)
    private boolean accountNonLocked = true;

    @Builder.Default
    @Column(name = "[credentials_non_expired]", nullable = false)
    private boolean credentialsNonExpired = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuthorities(getPermission());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private List<String> getPermission() {
        List<String> permissions = new ArrayList<>();
        List<Permission> collections = new ArrayList<>();
        for (Role role : roles) {
            permissions.add(role.getRole());
            collections.addAll(role.getPermissions());
        }

        for (Permission permission : collections) {
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for(String permission: permissions) {
            GrantedAuthority authority = new SimpleGrantedAuthority(permission);
            if (!authorities.contains(authority)) {
                authorities.add(authority);
            }
        }
        return authorities;
    }

}
