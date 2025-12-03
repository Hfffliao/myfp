import jakarta.servlet.DispatcherType;
import jakarta.servlet.MultipartConfigElement;
import love.linyi.config.MultiConfig;
import love.linyi.config.SpringConfig;
import love.linyi.config.SpringMvcConfig;
import love.linyi.controller.Code;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.ee10.servlet.*;
import org.eclipse.jetty.ee10.websocket.jakarta.server.config.JakartaWebSocketServletContainerInitializer;
import org.eclipse.jetty.http3.server.HTTP3ServerConnectionFactory;
import org.eclipse.jetty.quic.server.QuicServerConnector;
import org.eclipse.jetty.quic.server.ServerQuicConfiguration;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;
import java.nio.file.Path;
import java.util.EnumSet;

import static love.linyi.controller.Code.KeyStorePassword;
import static love.linyi.controller.Code.KeyStorePath;

public class JettyStarter {


    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServerConnector tlsConnector;
        // 1. 配置 TLS 上下文（HTTP/3 必须使用 TLS 1.3）
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(KeyStorePath+File.separator+"linyi.love.p12"); // 替换为实际证书路径
        sslContextFactory.setKeyStorePassword(KeyStorePassword); // 替换为证书密码

        sslContextFactory.setIncludeProtocols("TLSv1.3");

        // 2. HTTP 基础配置
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendXPoweredBy(false);
        httpConfig.setSendDateHeader(false);
        httpConfig.addCustomizer(new SecureRequestCustomizer());
      //2. 配置 QUIC (关键：指定 PEM 工作目录)
        Path pemWorkDir = Path.of(KeyStorePath);
        ServerQuicConfiguration serverQuicConfig = new ServerQuicConfiguration(sslContextFactory, pemWorkDir);
        serverQuicConfig.setMaxBidirectionalRemoteStreams(300); // 双向流
        serverQuicConfig.setMaxUnidirectionalRemoteStreams(300);
        //serverQuicConfig.setOutputBufferSize(20 * 1024 * 1024);
        HTTP3ServerConnectionFactory http3 = new HTTP3ServerConnectionFactory(serverQuicConfig);
        QuicServerConnector quicConnector = new QuicServerConnector(server, serverQuicConfig, http3);
        quicConnector.setPort(Code.jettyhttp3Andhttp2Port);


        server.addConnector(quicConnector);

        // 10. 添加 HTTP/2 连接器（用于客户端降级协商）
        // 创建 HTTPS 配置
        HttpConfiguration httpsConfig = new HttpConfiguration(httpConfig);
        httpsConfig.addCustomizer(new SecureRequestCustomizer());
        // 创建 HTTP/2 连接工厂
        org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory http2 =
                new org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory(httpsConfig);
        //http2.setMaxConcurrentStreams(800); // 最大并发流
        // 创建 ALPN 协议协商工厂
        ALPNServerConnectionFactory alpn =
                new ALPNServerConnectionFactory("h2","http/1.1");
        alpn.setDefaultProtocol("h1"); // 设置默认协议

        // 创建 TLS 连接器
        tlsConnector = new ServerConnector(
                server,
                new SslConnectionFactory(sslContextFactory, alpn.getProtocol()),
                alpn,
                http2
        );
        tlsConnector.setPort(Code.jettyhttp3Andhttp2Port);
        server.addConnector(tlsConnector);


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        JakartaWebSocketServletContainerInitializer.configure(context, null);
        // 添加一个 WebSocket 初始化 Servlet 来注册 WebSocket 端点。
        context.addServlet(MyJakartaWebSocketInitializerServlet.class, "/car/*");
        context.getSessionHandler().setMaxInactiveInterval(1800);
        AnnotationConfigWebApplicationContext webApplicationContextcontext=new AnnotationConfigWebApplicationContext();
        webApplicationContextcontext.register(SpringMvcConfig.class);

        AnnotationConfigWebApplicationContext applicationContext=new AnnotationConfigWebApplicationContext();
        applicationContext.register(SpringConfig.class);
        applicationContext.register(MultiConfig.class);
        ContextLoaderListener listener = new ContextLoaderListener(applicationContext);
        context.addEventListener(listener);
        DispatcherServlet dispatcherServlet=new DispatcherServlet(webApplicationContextcontext);
        ServletHolder servletHolder = context.addServlet(ResourceServlet.class, "/pages/*");
        // 添加 ResourceServlet 以从特定位置提供静态内容。
        ServletHolder dispatcherServletHolder = context.addServlet(dispatcherServlet, "/");
        dispatcherServletHolder.setInitOrder(1);

// 使用初始化参数配置 ResourceServlet。
        servletHolder.setInitParameter("baseResource", "C:\\Users\\HMCL\\javaeeeclipse-jee-2021-06-R-win32-x86_64\\workspase\\springmvc\\springmvc\\src\\main\\webapp\\pages");
        servletHolder.setInitParameter("pathInfoOnly", "true");
        servletHolder.setAsyncSupported(true);
        // 配置 WebSocket


        server.setHandler(context);
        server.start();
        server.join();
    }
    private static ServletContextHandler createServletContextHandler(
            WebApplicationContext rootContext,
            WebApplicationContext servletContext) {

     //   ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ServletContextHandler contextHandler = new ServletContextHandler("/");
        ServletHolder servletHolder= contextHandler.addServlet(DispatcherServlet.class,"/*");


        // 添加上下文属性，便于在 Servlet 和 Filter 中访问
        contextHandler.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootContext);

        // 添加 ContextLoaderListener 用于根上下文
       // contextHandler.addEventListener(new ContextLoaderListener(rootContext));

        // 创建并配置 DispatcherServlet


        servletHolder.setInitOrder(1);
        // 禁用默认 XML 配置加载
        servletHolder.setInitParameter("contextConfigLocation", "");

        // 配置文件上传 (对应 customizeRegistration 方法)
        MultipartConfigElement multipartConfig = new MultipartConfigElement(
                System.getProperty("java.io.tmpdir"),
                524288000,        // 500MB
                524288000,        // 500MB
                1000000           // 1MB 阈值
        );
        servletHolder.getRegistration().setMultipartConfig(multipartConfig);
        // 配置过滤器 (对应 getServletFilters 方法)
        //configureFilters(contextHandler, rootContext);

        return contextHandler;
    }
    private static void configureFilters(ServletContextHandler contextHandler, WebApplicationContext context) {
        // CORS 过滤器
        FilterHolder corsFilter = new FilterHolder(new DelegatingFilterProxy("corsFilter",context));
        contextHandler.addFilter(corsFilter, "/*", EnumSet.of(DispatcherType.REQUEST));

        // 字符编码过滤器
        FilterHolder charsetFilter = new FilterHolder(new DelegatingFilterProxy("charsetEncodingFilter", context));
        contextHandler.addFilter(charsetFilter, "/*", EnumSet.of(DispatcherType.REQUEST));

        // 认证过滤器
        FilterHolder authFilter = new FilterHolder(new DelegatingFilterProxy("authFilter", context));
        contextHandler.addFilter(authFilter, "/*", EnumSet.of(DispatcherType.REQUEST));
    }
}
