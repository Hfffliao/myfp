package love.linyi.service.camera.http3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
public class ContextHolder implements ApplicationContextAware {
    private static WebApplicationContext rootContext;
    private static WebApplicationContext webContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        // 确保传入的上下文是 WebApplicationContext
        if (applicationContext instanceof WebApplicationContext) {
            WebApplicationContext wac = (WebApplicationContext) applicationContext;

            if (wac.getParent() == null) {
                // 根上下文
                rootContext = wac;
            } else {
                // Web 上下文
                webContext = wac;
            }
        }
    }

    public static WebApplicationContext getRootContext() {
        return rootContext;
    }

    public static WebApplicationContext getWebContext() {
        return webContext;
    }
}