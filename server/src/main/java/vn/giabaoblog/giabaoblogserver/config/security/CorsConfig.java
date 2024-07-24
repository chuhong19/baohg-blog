package vn.giabaoblog.giabaoblogserver.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //corsConfiguration.addAllowedOrigin("*");
        //  Cross domain configuration error , take .allowedOrigins Replace with .allowedOriginPatterns that will do .
        //  Set the domain name that allows cross domain requests
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        //  Set allowed methods
        corsConfiguration.addAllowedMethod("*");
        //  Whether to allow certificates
        corsConfiguration.setAllowCredentials(true);
        //  Cross domain allow time
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }

    @Primary
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
}
