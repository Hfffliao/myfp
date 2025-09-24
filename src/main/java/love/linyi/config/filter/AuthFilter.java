package love.linyi.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AuthFilter implements Filter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // 获取现有 session，不存在则不创建

        // 定义不需要过滤的路径，比如登录页面和登录请求
        String loginUrl = httpRequest.getContextPath() + "/login";
        String registerUrl = httpRequest.getContextPath() + "/register";
        String logoutUrl = httpRequest.getContextPath() + "/logout";
        String main=httpRequest.getContextPath() + "/pages/main.html";
        String video=httpRequest.getContextPath() + "/video-ws";
        String currentUrl = httpRequest.getRequestURI();
        // 判断是否为静态资源路径，假设静态资源存于 /static 路径
        if(currentUrl.equals(httpRequest.getContextPath()+"/")){
            httpResponse.sendRedirect(main);
            return;
        }
        System.out.println("isStaticResource");
        boolean isStaticResource = currentUrl.startsWith(httpRequest.getContextPath() + "/pages");
        boolean isImageResource = currentUrl.startsWith(httpRequest.getContextPath() + "/image");
        boolean isfaviconResource = currentUrl.equals(httpRequest.getContextPath() + "/favicon.ico");
        boolean isvideoResource = currentUrl.equals(httpRequest.getContextPath() + "/video-ws");
        // 若请求的是登录页面或登录请求，直接放行
        if (currentUrl.equals(loginUrl) || currentUrl.equals(registerUrl)||currentUrl.equals(logoutUrl)|| isStaticResource||isImageResource||isfaviconResource||isvideoResource) {
            System.out.println("放行");
            chain.doFilter(request, response);
            return;
        }

        // 检查 session 中是否有用户信息
        if (session != null && session.getAttribute("user") != null&&session.getAttribute("id")!=null) {
            // 有用户信息，放行请求
            chain.doFilter(request, response);
        } else {
            // 没有用户信息，返回类似 ResponseEntity 的响应
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");

            Map<String, Object> body = new HashMap<>();
            body.put("success", false);
            body.put("message", "未授权，请先登录");

            try {
                String json = objectMapper.writeValueAsString(body);
                httpResponse.getWriter().write(json);
            } catch (IOException e) {
                throw new ServletException("写入响应时出错", e);
            }
        }
    }

    @Override
    public void destroy() {
        // 销毁方法
    }
}