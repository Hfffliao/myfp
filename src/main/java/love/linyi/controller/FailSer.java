package love.linyi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理登录失败请求的控制器
 */
@Controller
public class FailSer {

	/**
	 * 处理 GET 请求，返回登录失败信息
	 * @param response HttpServletResponse 对象，用于设置响应信息
	 * @throws
	 */
	@GetMapping("/fail")
	@ResponseBody
	public void doGet(HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write("登录失败");
	}


}