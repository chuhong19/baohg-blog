//package vn.giabaoblog.giabaoblogserver.config;
//
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.web.client.RestTemplate;
//import vn.giabaoblog.giabaoblogserver.services.authentication.DefaultTokenClaimComponentImpl;
//import vn.giabaoblog.giabaoblogserver.services.authentication.ITokenClaimComponent;
//
//@Configuration
//@EnableScheduling
//@RequiredArgsConstructor
//public class GlobalConfig {
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//
//    @Bean
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(100);
//        executor.setMaxPoolSize(100);
//        executor.setQueueCapacity(Integer.MAX_VALUE);
//        executor.setThreadNamePrefix("taskExecutor-");
//        executor.initialize();
//        return executor;
//    }
//
//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper mapper = new ModelMapper();
//        // TODO: mapper with default props
//        //mapper.
//        return mapper;
//    }
//
//    @Bean(value = "default")
//    public ITokenClaimComponent tokenClaimComponent() {
//        return new DefaultTokenClaimComponentImpl();
//    }
//
//    //@Bean
//    //@Primary
//    //public ObjectMapper objectMapper() {
//    //    return new ObjectMapper()
//    //            .registerModule(new JtsModule());
//    //}
//}