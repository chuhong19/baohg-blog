package vn.giabaoblog.giabaoblogserver.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import vn.giabaoblog.giabaoblogserver.services.authentication.ITokenClaimComponent;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AppConfigTest {

    @Autowired
    private ITokenClaimComponent tokenClaimComponent;

    @Autowired
    private HttpMessageConverter<BufferedImage> imageHttpMessageConverter;

    @Test
    public void testTokenClaimComponent() {
        assertNotNull(tokenClaimComponent, "TokenClaimComponent bean should not be null");
        assertTrue(tokenClaimComponent instanceof AppConfig.UserDetailsTokenClaim,
                "TokenClaimComponent should be an instance of UserDetailsTokenClaim");
    }

    @Test
    public void testImageHttpMessageConverter() {
        assertNotNull(imageHttpMessageConverter, "ImageHttpMessageConverter bean should not be null");
        assertTrue(imageHttpMessageConverter instanceof BufferedImageHttpMessageConverter,
                "ImageHttpMessageConverter should be an instance of BufferedImageHttpMessageConverter");
    }
}
