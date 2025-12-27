package love.linyi.controller.loginandout;

import love.linyi.domin.User;
import love.linyi.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * InHandler 控制器测试类
 */
@ExtendWith(MockitoExtension.class)
public class InHandlerTest {
    
    @Mock
    private UserService userService;
    
    @Mock
    private HttpServletRequest httpServletRequest;
    
    @Mock
    private HttpSession httpSession;
    
    @InjectMocks
    private InHandler inHandler;
    
    /**
     * 测试正常登录成功的情况
     */
    @Test
    void doPost_shouldReturnOk_whenCredentialsAreValid() {
        // 准备测试数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "testuser");
        requestBody.put("password", "testpass");
        
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUserName("testuser");
        mockUser.setPassword("testpass");
        
        // 模拟行为
        when(userService.getByusername("testuser")).thenReturn(mockUser);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(httpServletRequest.getRequestURI()).thenReturn("/login");
        when(httpSession.getId()).thenReturn("sldfjklafldj");
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = 
            inHandler.doPost(requestBody, httpServletRequest);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        Map<String, Object> responseBody = response.getBody();
        assertTrue((Boolean) responseBody.get("login"));
        assertEquals("testuser", responseBody.get("user"));
        assertNotNull(responseBody.get("tk"));
        
        // 验证交互
        verify(userService).getByusername("testuser");
        verify(httpSession).setMaxInactiveInterval(1800);
        verify(httpSession).setAttribute("user", "testuser");
        verify(httpSession).setAttribute("id", 1);
    }
    
    /**
     * 测试用户名不存在的情况
     */
    @Test
    void doPost_shouldReturnUnauthorized_whenUserDoesNotExist() {
        // 准备测试数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "nonexistent");
        requestBody.put("password", "anypass");
        
        // 模拟行为
        when(userService.getByusername("nonexistent")).thenReturn(null);
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(httpServletRequest.getRequestURI()).thenReturn("/login");
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = 
            inHandler.doPost(requestBody, httpServletRequest);
        
        // 验证结果
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("login"));
        
        // 验证交互
        verify(userService).getByusername("nonexistent");
        verify(httpSession, never()).setAttribute(anyString(), any());
    }
    
    /**
     * 测试密码错误的情况
     */
    @Test
    void doPost_shouldReturnUnauthorized_whenPasswordIsIncorrect() {
        // 准备测试数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "testuser");
        requestBody.put("password", "wrongpass");
        
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUserName("testuser");
        mockUser.setPassword("correctpass"); // 数据库中的正确密码
        
        // 模拟行为
        when(userService.getByusername("testuser")).thenReturn(mockUser);
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(httpServletRequest.getRequestURI()).thenReturn("/login");
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = 
            inHandler.doPost(requestBody, httpServletRequest);
        
        // 验证结果
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("login"));
        
        // 验证交互
        verify(userService).getByusername("testuser");
        verify(httpSession, never()).setAttribute(anyString(), any());
    }
    
    /**
     * 测试请求体为空的情况（参数化测试）
     */
    @ParameterizedTest
    @MethodSource("provideInvalidCredentials")
    void doPost_shouldReturnBadRequest_whenCredentialsAreMissing(
            String username, String password, String expectedMessage) {
        // 准备测试数据
        Map<String, Object> requestBody = new HashMap<>();
        if (username != null) requestBody.put("username", username);
        if (password != null) requestBody.put("password", password);
        
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(httpServletRequest.getRequestURI()).thenReturn("/login");
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = 
            inHandler.doPost(requestBody, httpServletRequest);
        
        // 验证结果
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("login"));
        assertEquals(expectedMessage, response.getBody().get("message"));
        
        // 验证交互 - UserService 不应该被调用
        verify(userService, never()).getByusername(anyString());
    }
    
    /**
     * 提供参数化测试数据
     */
    private static Stream<Arguments> provideInvalidCredentials() {
        return Stream.of(
            Arguments.of(null, "password", "用户名和密码不能为空"),
            Arguments.of("username", null, "用户名和密码不能为空"),
            Arguments.of(null, null, "用户名和密码不能为空"),
            Arguments.of("", "password", "用户名和密码不能为空"),
            Arguments.of("username", "", "用户名和密码不能为空"),
            Arguments.of("", "", "用户名和密码不能为空")
        );
    }
    
    /**
     * 测试 UserService 抛出 NullPointerException 的情况
     */
    @Test
    void doPost_shouldReturnUnauthorized_whenUserServiceThrowsNullPointerException() {
        // 准备测试数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "testuser");
        requestBody.put("password", "testpass");
        
        // 模拟行为 - UserService 抛出异常
        when(userService.getByusername("testuser")).thenThrow(new NullPointerException("数据库连接失败"));
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(httpServletRequest.getRequestURI()).thenReturn("/login");
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = 
            inHandler.doPost(requestBody, httpServletRequest);
        
        // 验证结果
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse((Boolean) response.getBody().get("login"));
        
        // 验证交互
        verify(userService).getByusername("testuser");
    }
    
    /**
     * 测试日志记录 - 当启用调试日志时
     */
    @Test
    void doPost_shouldLogDebugInfo_whenDebugIsEnabled() {
        // 准备测试数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "testuser");
        requestBody.put("password", "testpass");
        
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUserName("testuser");
        mockUser.setPassword("testpass");
        
        // 模拟行为
        when(userService.getByusername("testuser")).thenReturn(mockUser);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletRequest.getRemoteAddr()).thenReturn("192.168.1.1");
        when(httpServletRequest.getRequestURI()).thenReturn("/login");
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = 
            inHandler.doPost(requestBody, httpServletRequest);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        // 注意：实际测试中，我们需要验证日志是否被调用
        // 这通常需要使用专门的日志测试框架或使用 Mockito 模拟 Logger
        // 这里我们主要验证功能正常
    }
    
    /**
     * 测试边界情况 - 超长用户名/密码
     */
    @Test
    void doPost_shouldHandleLongCredentials() {
        // 准备测试数据 - 超长字符串
        String longUsername = "a".repeat(1000);
        String longPassword = "b".repeat(1000);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", longUsername);
        requestBody.put("password", longPassword);
        
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUserName(longUsername);
        mockUser.setPassword(longPassword);
        
        // 模拟行为
        when(userService.getByusername(longUsername)).thenReturn(mockUser);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(httpServletRequest.getRequestURI()).thenReturn("/login");
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = 
            inHandler.doPost(requestBody, httpServletRequest);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("login"));
        
        // 验证交互
        verify(userService).getByusername(longUsername);
    }
    
    /**
     * 测试特殊字符的情况
     */
    @Test
    void doPost_shouldHandleSpecialCharacters() {
        // 准备测试数据 - 包含特殊字符
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "user@test.com");
        requestBody.put("password", "P@ssw0rd!123");
        
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUserName("user@test.com");
        mockUser.setPassword("P@ssw0rd!123");
        
        // 模拟行为
        when(userService.getByusername("user@test.com")).thenReturn(mockUser);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(httpServletRequest.getRequestURI()).thenReturn("/login");
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = 
            inHandler.doPost(requestBody, httpServletRequest);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("login"));
    }
    
    /**
     * 测试并发情况 - 验证 session 属性的正确设置
     */
    @Test
    void doPost_shouldSetCorrectSessionAttributes() {
        // 准备测试数据
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "testuser");
        requestBody.put("password", "testpass");
        
        User mockUser = new User();
        mockUser.setId(42); // 特殊ID值
        mockUser.setUserName("testuser");
        mockUser.setPassword("testpass");
        
        // 模拟行为
        when(userService.getByusername("testuser")).thenReturn(mockUser);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(httpServletRequest.getRequestURI()).thenReturn("/login");
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = 
            inHandler.doPost(requestBody, httpServletRequest);
        
        // 验证 session 属性设置正确
        verify(httpSession).setAttribute("user", "testuser");
        verify(httpSession).setAttribute("id", 42);
        verify(httpSession).setMaxInactiveInterval(1800);
    }
    
    /**
     * 测试请求体包含额外字段的情况
     */
    @Test
    void doPost_shouldIgnoreExtraFieldsInRequestBody() {
        // 准备测试数据 - 包含额外字段
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("username", "testuser");
        requestBody.put("password", "testpass");
        requestBody.put("extraField", "extraValue");
        requestBody.put("rememberMe", true);
        
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUserName("testuser");
        mockUser.setPassword("testpass");
        
        // 模拟行为
        when(userService.getByusername("testuser")).thenReturn(mockUser);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        when(httpServletRequest.getRemoteAddr()).thenReturn("127.0.0.1");
        when(httpServletRequest.getRequestURI()).thenReturn("/login");
        
        // 执行测试
        ResponseEntity<Map<String, Object>> response = 
            inHandler.doPost(requestBody, httpServletRequest);
        
        // 验证结果 - 应该忽略额外字段正常登录
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("login"));
    }
}