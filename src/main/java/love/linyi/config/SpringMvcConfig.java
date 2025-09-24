package love.linyi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

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

        // 部署环境映射，假设 HTML 文件在类路径下的 META-INF/resources/pages 目录
    }
//    @Bean(name = "multipartResolver")
//    public CommonsMultipartResolver multipartResolver() {
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        // 最大上传文件大小 100MB
//        resolver.setMaxUploadSize(104857600);
//        resolver.setDefaultEncoding("UTF-8");
//        return resolver;
//    }
}
