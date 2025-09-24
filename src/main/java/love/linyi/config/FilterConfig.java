package love.linyi.config;
import love.linyi.config.filter.AuthFilter;
import love.linyi.config.filter.CharsetEncodingFilter;
import love.linyi.config.filter.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter();
    }
    @Bean
    public CharsetEncodingFilter charsetEncodingFilter() {
        return new CharsetEncodingFilter();
    }
    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }
}