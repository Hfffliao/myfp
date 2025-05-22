package love.linyi.config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan({"love.linyi.service","love.linyi.dao","love.linyi.config","love.linyi.domin"})
@PropertySource("classpath:love/linyi/config/jdbc.properties")
@MapperScan("love.linyi.dao")
@Import({JdbcConfig.class,MybatisConfig.class})
public class SpringConfig {
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("");
        viewResolver.setSuffix("");
        return viewResolver;
    }
}