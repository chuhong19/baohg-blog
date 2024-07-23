package vn.giabaoblog.giabaoblogserver.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import vn.giabaoblog.giabaoblogserver.data.domains.User;
import vn.giabaoblog.giabaoblogserver.services.UserService;
import vn.giabaoblog.giabaoblogserver.services.authentication.DefaultTokenClaimComponentImpl;
import vn.giabaoblog.giabaoblogserver.services.authentication.ITokenClaimComponent;

import java.awt.image.BufferedImage;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class AppConfig {
    private final UserService userService;

    @Bean
    public ITokenClaimComponent tokenClaimComponent() {
        return new UserDetailsTokenClaim(userService);
    }

    @AllArgsConstructor
    public static class UserDetailsTokenClaim extends DefaultTokenClaimComponentImpl {
        private final UserService userService;

        @Override
        public Map<String, Object> getClaims(Map<String, Object> extraClaims, User userDetails, long expiration) {
            extraClaims = super.getClaims(extraClaims, userDetails, expiration);
            boolean hasUserRole = userDetails.getRoles().stream()
                    .anyMatch(role -> "USER".equals(role.getRole()));
            return extraClaims;
        }
    }

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }
}
