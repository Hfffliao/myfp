package love.linyi.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import love.linyi.common.context.UserContext;

import java.io.IOException;
import java.util.Set;

public class AuthFilter implements Filter {
    private ObjectMapper objectMapper;
    
    private static final Set<String> EXACT_MATCH_WHITELIST = Set.of(
            "/login", "/register", "/logout", "/car/user", "/car", "/favicon.ico"
    );
    
    private static final Set<String> PREFIX_MATCH_WHITELIST = Set.of(
            "/pages", "/image", "/video-stream", "/swagger-ui", "/v3"
    );

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String contextPath = httpRequest.getContextPath();
        String currentUrl = httpRequest.getRequestURI();
        String path = currentUrl.substring(contextPath.length());

        if (path.isEmpty() || "/".equals(path)) {
            httpResponse.sendRedirect(contextPath + "/pages/main.html");
            return;
        }

        if (isWhitelisted(path)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            HttpSession session = httpRequest.getSession(false);
            if (session == null) {
                sendUnauthorized(httpResponse);
                return;
            }
            
            String username = (String) session.getAttribute("user");
            Integer userId = (Integer) session.getAttribute("id");
            
            if (username == null || userId == null || userId == 0) {
                sendUnauthorized(httpResponse);
                return;
            }

            UserContext.setUserInfo(username, userId);
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }

    private boolean isWhitelisted(String path) {
        if (EXACT_MATCH_WHITELIST.contains(path)) {
            return true;
        }
        for (String prefix : PREFIX_MATCH_WHITELIST) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private void sendUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\":false,\"message\":\"未授权，请先登录\"}");
    }

    @Override
    public void destroy() {
    }
}