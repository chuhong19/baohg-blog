package vn.giabaoblog.giabaoblogserver.data.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshResponse {

    private String accessToken;

    private String refreshToken;

    public TokenRefreshResponse(String token, String requestRefreshToken) {
        this.accessToken = token;
        this.refreshToken = requestRefreshToken;
    }
}
