package vn.giabaoblog.giabaoblogserver.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.giabaoblog.giabaoblogserver.data.domains.PostComment;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    @Query("SELECT COUNT(pc) FROM PostComment pc WHERE pc.postId = :postId")
    int getCommentCountByPostId(@Param("postId") Long postId);

}
