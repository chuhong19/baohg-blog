package vn.giabaoblog.giabaoblogserver.data.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.giabaoblog.giabaoblogserver.data.enums.LikeStatus;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "posts_like")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[id]")
    public Long id;

    @Column(name= "post_id", nullable = false)
    public Long postId;

    @Column(name= "user_id", nullable = false)
    public Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name= "like_status", nullable = false)
    public LikeStatus likeStatus;

    public PostLike(Long postId, Long userId, LikeStatus likeStatus) {
        this.postId = postId;
        this.userId = userId;
        this.likeStatus = likeStatus;
    }
}
