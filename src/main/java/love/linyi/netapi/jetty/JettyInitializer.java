package love.linyi.netapi.jetty;

import ch.qos.logback.classic.Level;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.MultipartConfigElement;
import love.linyi.controller.Code;
import love.linyi.service.camera.http3.ContextHolder;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.http3.server.HTTP3ServerConnectionFactory;
import org.eclipse.jetty.quic.server.QuicServerConnector;
import org.eclipse.jetty.quic.server.ServerQuicConfiguration;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import java.io.File;
import java.nio.file.Path;
import java.util.EnumSet;

import static love.linyi.controller.Code.KeyStorePassword;
import static love.linyi.controller.Code.KeyStorePath;

@Component
public class JettyInitializer {
    private Server server;
    ServerConnector tlsConnector;
    public void removeHttp2Connector() {
        for (Connector connector : server.getConnectors()) {
            if (connector instanceof ServerConnector) {
                server.removeConnector(connector);
                System.out.println("removeHTTP/2connector");
            }
        }
    }
    public void addHttp2Connector() {
        server.addConnector(tlsConnector);
    }
    @PostConstruct
    public void startJettyInThread() throws Exception {

        // 设置全局日志级别为INFO，只记录重要信息
        ch.qos.logback.classic.Logger rootLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);

// Jetty核心组件：只记录WARN及以上级别
        ch.qos.logback.classic.Logger jettyLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.eclipse.jetty");
        jettyLogger.setLevel(Level.WARN);

// I/O操作：只记录错误
        ch.qos.logback.classic.Logger jettyIoLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.eclipse.jetty.io");
        jettyIoLogger.setLevel(Level.ERROR);

// HTTP/2和QUIC：关闭详细日志
        ch.qos.logback.classic.Logger jettyHttp2Logger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.eclipse.jetty.http2");
        jettyHttp2Logger.setLevel(Level.OFF);

        ch.qos.logback.classic.Logger jettyHttp3Logger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.eclipse.jetty.http3");
        jettyHttp3Logger.setLevel(Level.OFF);

// 实用工具类：关闭日志
        ch.qos.logback.classic.Logger jettyUtilLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.eclipse.jetty.util");
        jettyUtilLogger.setLevel(Level.OFF);

// HPACK压缩：关闭详细日志
        ch.qos.logback.classic.Logger hpackLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.eclipse.jetty.http2.hpack");
        hpackLogger.setLevel(Level.OFF);

// 关键：开启Spring和您应用的DEBUG日志
        ch.qos.logback.classic.Logger springLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("org.springframework");
        springLogger.setLevel(Level.DEBUG);

        ch.qos.logback.classic.Logger appLogger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("love.linyi");
        appLogger.setLevel(Level.DEBUG);
        new Thread(() -> {
            try {
                startJetty();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
    public void startJetty() throws Exception {
        server = new Server();
        // 1. 配置 TLS 上下文（HTTP/3 必须使用 TLS 1.3）
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStorePath(KeyStorePath+File.separator+"linyi.love.p12"); // 替换为实际证书路径
        sslContextFactory.setKeyStorePassword(KeyStorePassword); // 替换为证书密码

        sslContextFactory.setIncludeProtocols("TLSv1.3");

        // 2. HTTP 基础配置
        HttpConfiguration httpConfig = new HttpConfiguration();
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
        alpn.setDefaultProtocol("h2"); // 设置默认协议

        // 创建 TLS 连接器
        tlsConnector = new ServerConnector(
                server,
                new SslConnectionFactory(sslContextFactory, alpn.getProtocol()),
                alpn,
                http2
        );
        tlsConnector.setPort(Code.jettyhttp3Andhttp2Port);
        server.addConnector(tlsConnector);






        //区别于用ServletContainerConfig,这里使用ServletContextHandler来配置DispatcherServlet
        //和root上下文





        // 4. 创建 Spring 应用上下文
        WebApplicationContext servletContext = ContextHolder.getWebContext();;
        WebApplicationContext rootContext = ContextHolder.getRootContext();
        // 5. 创建并配置 DispatcherServlet
        // 5. 创建并配置 ServletContextHandler
        ServletContextHandler contextHandler = createServletContextHandler(rootContext, servletContext);
        // 6. 配置处理器树
        ContextHandlerCollection handlers = new ContextHandlerCollection();
       // handlers.addHandler(qosHandler);
        handlers.addHandler(contextHandler);

        server.setHandler(handlers);



//        server.setHandler(new Handler.Abstract()
//        {
//            @Override
//            public boolean handle(Request request, Response response, Callback callback)
//            {
//                // 成功回调，表示请求/响应处理已完成。
//                callback.succeeded();
//                return true;
//            }
//        });

        // 13. 启动服务器
        server.start();
        server.join();
        System.out.println("Jetty server started successfully");
        System.out.println("HTTP/3 listening on port: " + 8443);
        System.out.println("HTTP/2 listening on port: 8443");

    }
    private ServletContextHandler createServletContextHandler(
            WebApplicationContext rootContext,
            WebApplicationContext servletContext) {

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");

        // 添加上下文属性，便于在 Servlet 和 Filter 中访问
        contextHandler.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootContext);

        // 添加 ContextLoaderListener 用于根上下文
       // contextHandler.addEventListener(new ContextLoaderListener(rootContext));

        // 创建并配置 DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(servletContext);
        ServletHolder servletHolder = new ServletHolder("dispatcher", dispatcherServlet);
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

        // 添加 DispatcherServlet
        contextHandler.addServlet(servletHolder, "/*");

        // 配置过滤器 (对应 getServletFilters 方法)
        configureFilters(contextHandler, rootContext);

        return contextHandler;
    }
    private void configureFilters(ServletContextHandler contextHandler, WebApplicationContext context) {
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
    @PreDestroy
    public void stopJetty() throws Exception {
        if (server != null) {
            server.stop();
            System.out.println("Jetty server stopped");
        }
    }
}