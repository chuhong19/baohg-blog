package vn.giabaoblog.giabaoblogserver.data.dto.shortName;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrUpdatePostDTO implements Serializable {

    public Long postId;
    public String title;
    public String content;

}
