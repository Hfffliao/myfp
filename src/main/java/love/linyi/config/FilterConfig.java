package love.linyi.config;
import love.linyi.config.filter.AuthFilter;
import love.linyi.config.filter.CharsetEncodingFilter;
import love.linyi.config.filter.CorsFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter();
    }
    
    @Bean
    public CharsetEncodingFilter charsetEncodingFilter() {
        return new CharsetEncodingFilter();
    }
    
    @Bean
    public AuthFilter authFilter(ObjectMapper objectMapper) {
        AuthFilter filter = new AuthFilter();
        filter.setObjectMapper(objectMapper);
        return filter;
    }
}