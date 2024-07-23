package vn.giabaoblog.giabaoblogserver.data.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "posts_report")
public class PostReport extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "[id]")
    public Long id;

    @Column(name= "post_id", nullable = false)
    public Long postId;

    @Column(name= "author_id", nullable = false)
    public Long authorId;

    @Column(name= "report_user_id", nullable = false)
    public Long reportUserId;

}
