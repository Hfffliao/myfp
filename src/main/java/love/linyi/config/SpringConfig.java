package love.linyi.config;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan({"love.linyi.service","love.linyi.dao","love.linyi.config","love.linyi.domin"})
@PropertySource("classpath:love/linyi/config/jdbc.properties")
@MapperScan("love.linyi.dao")
@Import({JdbcConfig.class,MybatisConfig.class})
public class SpringConfig {
}