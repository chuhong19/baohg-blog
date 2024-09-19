package vn.giabaoblog.giabaoblogserver.data.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String to;
    private String content;
    private String type; // registration or reset

}

