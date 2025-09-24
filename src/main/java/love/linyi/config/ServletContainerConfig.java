package love.linyi.config;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import javax.servlet.Filter;

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
}
