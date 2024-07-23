package vn.giabaoblog.giabaoblogserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.giabaoblog.giabaoblogserver.data.domains.PostLike;
import vn.giabaoblog.giabaoblogserver.data.enums.LikeStatus;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByPostIdAndUserIdAndLikeStatus(Long postId, Long userId, LikeStatus likeStatus);

    @Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.postId = :postId AND pl.likeStatus = 'LIKE'")
    Integer getLikeCountByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(pl) FROM PostLike pl WHERE pl.postId = :postId AND pl.likeStatus = 'DISLIKE'")
    Integer getDislikeCountByPostId(@Param("postId") Long postId);

}
