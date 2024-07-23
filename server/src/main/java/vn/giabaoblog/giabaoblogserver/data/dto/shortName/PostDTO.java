package vn.giabaoblog.giabaoblogserver.data.dto.shortName;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.LastModifiedDate;
import vn.giabaoblog.giabaoblogserver.data.domains.Auditable;
import vn.giabaoblog.giabaoblogserver.data.domains.Post;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.data.repository.UserRepository;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class PostDTO extends Auditable implements Serializable {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;
    @JsonProperty("authorId")
    private Long authorId;
    @JsonProperty("authorName")
    private String authorName;
    @JsonProperty("isBanned")
    private boolean isBanned;

    private Date createdAt;

    private Date updatedAt;

    public PostDTO(PostDTO post) {
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorId = post.getAuthorId();
        this.isBanned = post.isBanned();
        this.createdAt = post.createdAt;
        this.updatedAt = post.updatedAt;
    }

    public PostDTO(Post post, String authorName) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorId = post.getAuthorId();
        this.isBanned = post.isBanned();
        this.authorName = authorName;
        this.createdAt = post.createdAt;
        this.updatedAt = post.updatedAt;
    }

}
