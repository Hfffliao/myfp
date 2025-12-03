package love.linyi.controller.loginandout;

import love.linyi.domin.User;
import love.linyi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * InHandler 类是一个 RESTful 风格的控制器，负责处理用户登录相关的请求。
 * 该类使用 Spring MVC 框架，通过调用 UserService 来验证用户信息。
 */
@RestController
//@CrossOrigin
public class InHandler {

    /**
     * 注入 UserService 实例，用于处理用户相关的业务逻辑。
     */
    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(InHandler.class);
    /**
     * 处理用户登录的 POST 请求。
     * 从请求体中获取用户名和密码，调用 UserService 验证用户信息。
     * 若验证通过，创建 session 并返回包含 token 的响应；若验证失败，返回相应的错误信息。
     *
     * @param requestBody 包含用户名和密码的请求体，类型为 Map<String, Object>
     * @param request HttpServletRequest 对象，用于获取 session 信息
     * @return ResponseEntity<Map<String, Object>> 包含登录结果和相关信息的响应实体
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> doPost(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {
        if(logger.isDebugEnabled()){
            logger.debug("requestIP: {}", request.getRemoteAddr());
            logger.debug("RequestURI: {}", request.getRequestURI());
            logger.debug("requestBody: {}", requestBody);
        }
        // 用于存储响应信息的 Map
        Map<String, Object> response = new HashMap<>();
        try {
            // 从请求体中获取用户名和密码
            String name = (String) requestBody.get("username");
            String word = (String) requestBody.get("password");

            // 检查用户名和密码是否为空
            if (name == null || word == null) {
                // 若为空，设置登录失败标志和错误信息
                response.put("login", false);
                response.put("message", "用户名和密码不能为空");
                // 返回 HTTP 400 Bad Request 响应
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // 调用 UserService 获取用户信息
            User user = userService.getByusername(name);

            int id = user.getId();
            // 验证用户信息是否正确
            if (user != null && user.getPassword().equals(word)) {
                // 获取或创建 session
                HttpSession session = request.getSession();
                // 设置 session 过期时间为 1800 秒（即 30 分钟）
                session.setMaxInactiveInterval(1800);
                // 将 session ID 作为 token 添加到响应中
                response.put("tk", session.getId());
                //System.out.println("sessionid:" + session.getId());
                // 将用户名存储到 session 中
                session.setAttribute("user", user.getUserName());
                session.setAttribute("id", id);

                // 设置登录成功标志
                response.put("login", true);

                response.put("user", user.getUserName());
                // 返回 HTTP 200 OK 响应
                return ResponseEntity.ok(response);
            } else {
                // 若验证失败，设置登录失败标志
                response.put("login", false);
                // 返回 HTTP 401 Unauthorized 响应
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }catch (NullPointerException e){
            // 捕获任何异常，设置登录失败标志和错误信息
            response.put("login", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
//    private String getClientIp(HttpServletRequest request) {
//        String ip = request.getHeader("X-Forwarded-For");
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_CLIENT_IP");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//        }
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            ip = request.getRemoteAddr();
//        }
//        // 如果经过多个代理，取第一个 IP
//        if (ip != null && ip.contains(",")) {
//            ip = ip.split(",")[0].trim();
//        }
//        return ip;
//    }

}
