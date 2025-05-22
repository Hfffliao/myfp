package love.linyi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理成功相关请求的控制器
 */
@Controller
@RequestMapping("/suc")
public class SucSer {
	/**
	 * 处理 GET 请求
	 * @param request HttpServletRequest 对象
	 * @param response HttpServletResponse 对象
	 * @return ModelAndView 对象
	 */
	@GetMapping
	public ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ModelAndView modelAndView = new ModelAndView();
		if (request.getSession().getAttribute("user") != null) {
			response.setContentType("text/html;charset=utf-8");
			modelAndView.setViewName("redirect:" + Code.host + "pages/maint.jsp");
		} else {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write("请登录");
			modelAndView.setViewName("redirect:" + Code.host + "main.jsp");
		}
		return modelAndView;
	}
}