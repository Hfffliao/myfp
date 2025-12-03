package love.linyi.config;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
@ComponentScan("love.linyi.controller")
@EnableWebMvc
public class SpringMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");
        registry.addResourceHandler("/pages/**")
                .addResourceLocations("/pages/");
        registry.addResourceHandler("/siwei/**")
                .addResourceLocations("/siwei/");
        registry.addResourceHandler("/image/**")
                .addResourceLocations("/image/");
//                setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS).cachePublic());
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("/favicon.ico");
        registry.addResourceHandler("/main.html")
                .addResourceLocations("/main.html");

        // SpringDoc UI资源
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
                .resourceChain(false);


        // 添加 OpenAPI JSON 描述映射
        registry.addResourceHandler("/v3/api-docs/**")
                .addResourceLocations("classpath:/META-INF/resources/v3/api-docs/");

    }
}
