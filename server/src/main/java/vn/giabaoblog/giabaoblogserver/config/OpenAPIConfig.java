//package vn.giabaoblog.giabaoblogserver.config;
//
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.info.License;
//import io.swagger.v3.oas.models.servers.Server;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@Configuration
//@RequiredArgsConstructor
//public class OpenAPIConfig {
//    @Value("${vn.giabaoblog.giabaoblogserver.contact-email}")
//    private String contactEmail;
//    @Value("${vn.giabaoblog.giabaoblogserver.contact-name}")
//    private String contactName;
//    @Value("${vn.giabaoblog.giabaoblogserver.contact-url}")
//    private String contactUrl;
//    @Value("${vn.giabaoblog.giabaoblogserver.name}")
//    private String name;
//    @Value("${vn.giabaoblog.giabaoblogserver.description}")
//    private String description;
//    @Value("${vn.giabaoblog.giabaoblogserver.version}")
//    private String version;
//    @Value("${vn.giabaoblog.giabaoblogserver.termsOfService}")
//    private String termsOfService;
//    @Value("${vn.giabaoblog.giabaoblogserver.licenseName}")
//    private String licenseName;
//    @Value("${vn.giabaoblog.giabaoblogserver.licenseUrl}")
//    private String licenseUrl;
//    @Value("${vn.giabaoblog.giabaoblogserver.dev-url}")
//    private String devUrl;
//    @Value("${vn.giabaoblog.giabaoblogserver.staging-url}")
//    private String stagingUrl;
//    @Value("${vn.giabaoblog.giabaoblogserver.prod-url}")
//    private String prodUrl;
//
//    @Bean
//    public OpenAPI myOpenAPI() {
//        Server stagingServer = new Server();
//        stagingServer.setUrl(stagingUrl);
//        stagingServer.setDescription("Server URL in Staging environment");
//
//        Server devServer = new Server();
//        devServer.setUrl(devUrl);
//        devServer.setDescription("Server URL in Development environment");
//
//        Server prodServer = new Server();
//        prodServer.setUrl(prodUrl);
//        prodServer.setDescription("Server URL in Production environment");
//
//        Contact contact = new Contact();
//        contact.setEmail(contactEmail);
//        contact.setName(contactName);
//        contact.setUrl(contactUrl);
//
//        License mitLicense = new License().name(licenseName).url(licenseUrl);
//
//        Info info = new Info().title(name).version(version).contact(contact).description(description).termsOfService(termsOfService).license(mitLicense);
//
//        return new OpenAPI().info(info).servers(List.of(stagingServer, devServer, prodServer));
//    }
//}
