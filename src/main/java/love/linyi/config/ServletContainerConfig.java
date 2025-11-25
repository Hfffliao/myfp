package love.linyi.config;
import jakarta.servlet.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import java.util.Map;

//可以去看"\liaoyis_online\spring\springmvc\webapplicationinitializer_Understand.md"来了解这个类配置webapp的原理
public class ServletContainerConfig extends AbstractDispatcherServletInitializer {

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext context=new AnnotationConfigWebApplicationContext();
        context.register(SpringMvcConfig.class);
        return context;
    }
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        AnnotationConfigWebApplicationContext context=new AnnotationConfigWebApplicationContext();
        context.register(SpringConfig.class);
        context.register(MultiConfig.class);
        return context;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
    @Override
    protected Filter[] getServletFilters() {
        // 注册 CorsFilter
        DelegatingFilterProxy corsFilterProxy = new DelegatingFilterProxy("corsFilter");
        DelegatingFilterProxy charsetEncodingFilterProxy = new DelegatingFilterProxy("charsetEncodingFilter");
        DelegatingFilterProxy authFilterProxy = new DelegatingFilterProxy("authFilter");

        return new Filter[]{corsFilterProxy, charsetEncodingFilterProxy, authFilterProxy};
    }
    //在这里可以拿到servletContext，不是规范的方法，只是我看源码的理解
//    @Override
//    protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter){
//        servletContext.getInitParameterNames().asIterator().forEachRemaining(System.out::println);
//        System.out.println("wo na dao la servletContext");
//        return super.registerServletFilter(servletContext, filter);
//    }
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        // 文件上传配置
        MultipartConfigElement multipartConfig = new MultipartConfigElement(
                System.getProperty("java.io.tmpdir"),               // 临时文件存储位置（空表示系统默认）
                524288000,        // 最大文件大小 (500MB)
                524288000,        // 最大请求大小 (500MB)
                500000*2                 // 文件大小阈值（0表示所有文件都写入磁盘）
        );
        registration.setMultipartConfig(multipartConfig);

//        registration.setInitParameter("multipart.maxFileSize", "1000MB"); // 单个文件大小
//        registration.setInitParameter("multipart.maxRequestSize", "5000MB"); // 总请求大小
//        // 可选：添加其他 Servlet 配置
//        registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
//        registration.setInitParameter("readOnly", "false");
    }
}
