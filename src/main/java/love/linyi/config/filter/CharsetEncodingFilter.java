
package love.linyi.config.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CharsetEncodingFilter implements Filter {
    // 默认字符编码
    private static final String DEFAULT_ENCODING = "UTF-8";
    private String encoding;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 从过滤器配置中获取字符编码，如果没有配置则使用默认编码
        encoding = filterConfig.getInitParameter("encoding");
        if (encoding == null) {
            encoding = DEFAULT_ENCODING;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 将请求和响应对象转换为 HTTP 相关对象
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if(encoding==null){
            encoding=DEFAULT_ENCODING;
        }
        // 设置请求的字符编码
        request.setCharacterEncoding(encoding);
        // 设置响应的字符编码和内容类型
        response.setCharacterEncoding(encoding);

        // 继续执行后续的过滤器和请求处理逻辑
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 过滤器销毁时的操作，此处无需特殊处理
    }
}
