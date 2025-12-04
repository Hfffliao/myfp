package love.linyi.config;

//import love.linyi.netapi.websocket.VideoStreamWebSocketHandler;
import love.linyi.netapi.websocket.VideoStreamWebSocketHandler;
import love.linyi.netapi.websocket.WebSocketToEsp8266;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;


@Configuration
@ComponentScan({"love.linyi.service","love.linyi.dao","love.linyi.config","love.linyi.domin","love.linyi.netapi"})
@PropertySource("classpath:love/linyi/config/jdbc.properties")
@EnableAspectJAutoProxy
@MapperScan("love.linyi.dao")
@Import({JdbcConfig.class,MybatisConfig.class})
//@EnableJpaRepositories(basePackages = "love.linyi.dao")
@EnableTransactionManagement
@EnableScheduling
@EnableWebMvc
@EnableWebSocket
public class SpringConfig implements WebMvcConfigurer, WebSocketConfigurer{
    @Autowired
    private VideoStreamWebSocketHandler videoStreamWebSocketHandler;
    @Autowired
    private WebSocketToEsp8266 webSocketToEsp8266;
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("TaskExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("TaskScheduler-");
        scheduler.initialize();
        return scheduler;
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(videoStreamWebSocketHandler, "/car/user")
                .setAllowedOrigins("*")
                .addInterceptors(new HttpSessionHandshakeInterceptor());;
        registry.addHandler(webSocketToEsp8266, "/car")
                .setAllowedOrigins("*");
    }
}