package vn.giabaoblog.giabaoblogserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.giabaoblog.giabaoblogserver.data.domains.Post;
import vn.giabaoblog.giabaoblogserver.data.domains.UserFollow;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {

    @Override
    Optional<UserFollow> findById(Long id);

    List<UserFollow> findByUserId(Long userId);
    Optional<UserFollow> findByUserIdAndFollowId(Long userId, Long followId);
}
