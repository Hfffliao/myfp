package love.linyi.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

//@Configuration
public class SpringMvcSupport extends WebMvcConfigurationSupport {
   // @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/pages/**")
//                .addResourceLocations("/pages/");
//        // 部署环境映射，假设 HTML 文件在类路径下的 META-INF/resources/pages 目录
//    }
    // 如果你的 HTML 文件在 webapp 目录下，可以这样配置

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 添加处理 JSON 的消息转换器
        converters.add(new MappingJackson2HttpMessageConverter());
        // 调用父类方法添加默认的消息转换器
        super.configureMessageConverters(converters);
    }
}
