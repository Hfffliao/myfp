package love.linyi.config.filter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CorsFilter implements Filter {
    // 允许跨域的域名列表
    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "https://linyi.love:8443",
            "http://linyi.love:8443",
            "https://linyi.love",
            "http://linyi.love"
    );
    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String method = httpRequest.getMethod().toLowerCase();
        // 获取请求的Origin
        String origin = httpRequest.getHeader("Origin");
        // 检查Origin是否在允许列表中
        if (ALLOWED_ORIGINS.contains(origin)) {
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
        }
        // 允许携带凭证
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        // 允许的请求方法
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        // 允许的请求头，添加 Cache-Control、Pragma 和 Expires
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Cache-Control, Pragma, Expires,Alt-Svc");
        // 暴露的响应头
        httpResponse.setHeader("Access-Control-Expose-Headers", "TK,Content-Disposition,Alt-Svc");

        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        // 表示支持http3
        httpResponse.setHeader("Alt-Svc", "h3=\":8443\"");
        if ("options".equals(method)) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        HttpSession session = httpRequest.getSession();
        httpResponse.setHeader("TK", session.getId());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 销毁逻辑，若无需特殊处理，可留空
    }
}
