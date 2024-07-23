package vn.giabaoblog.giabaoblogserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import vn.giabaoblog.giabaoblogserver.data.domains.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);

    @Query(value = "select count(u)>0 " +
            "from User u " +
            "where u.username = ?1 or u.email = ?1")
    boolean existsByUsernameOrEmail(final String username);

    User findByUsername(final String username);

    User findByEmail(final String email);

    Optional<User> findByResetToken(final String resetToken);

    @Query(value = "select u " +
            "from User u " +
            "where u.username = ?1 or u.email = ?1")
    Optional<User> findByUsernameOrEmail(final String username);

}
