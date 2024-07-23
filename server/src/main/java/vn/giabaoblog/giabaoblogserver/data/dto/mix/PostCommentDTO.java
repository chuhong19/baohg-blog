package vn.giabaoblog.giabaoblogserver.data.dto.mix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.giabaoblog.giabaoblogserver.data.domains.PostComment;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCommentDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private String username;

    private Date createdAt;

    private Date updatedAt;

    public PostCommentDTO(PostComment postComment, String username) {
        this.id = postComment.getId();
        this.postId = postComment.getPostId();
        this.userId = postComment.getUserId();
        this.content = postComment.getContent();
        this.createdAt = postComment.createdAt;
        this.updatedAt = postComment.updatedAt;
        this.username = username;
    }
}
