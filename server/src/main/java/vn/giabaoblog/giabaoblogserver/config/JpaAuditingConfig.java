package vn.giabaoblog.giabaoblogserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

@Configuration
public class JpaAuditingConfig {

    private static String MACHINE_ID;

    public static String getMachineId() throws UnknownHostException {
        if (!StringUtils.hasLength(MACHINE_ID)) {
            MACHINE_ID = InetAddress.getLocalHost().getHostName();
        }
        return MACHINE_ID;
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            try {
                // You can customize how to obtain the machine ID here
                // For example, use InetAddress to get the machine name
                return Optional.of(getMachineId());
            } catch (UnknownHostException e) {
                return Optional.of("UNKNOWN");
            }
        };
    }
}
