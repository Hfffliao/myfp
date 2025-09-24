package love.linyi.config.filter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CorsFilter implements Filter {

    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String method = httpRequest.getMethod().toLowerCase();
        // 设置允许的源，这里指定具体的客户端源
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://linyi.love:25565");
        // 允许携带凭证
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        // 允许的请求方法
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        // 允许的请求头，添加 Cache-Control、Pragma 和 Expires
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Cache-Control, Pragma, Expires");
        // 暴露的响应头
        httpResponse.setHeader("Access-Control-Expose-Headers", "TK,Content-Disposition");

        httpResponse.setHeader("Access-Control-Max-Age", "3600");
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
