package love.linyi.controller;

import love.linyi.domin.User;
import love.linyi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class InHandler {

	// 使用 @Autowired 注解自动注入 UserService
	@Autowired
	private UserService userService;

	/**
	 * 处理 POST 请求，实现用户登录逻辑
	 * @param name 用户名
	 * @param word 密码
	 * @param request HttpServletRequest 对象，用于获取会话
	 * @return 登录结果对应的视图名
	 */
	@PostMapping("/login")
	public String doPost(@RequestParam("username") String name, @RequestParam("password") String word, HttpServletRequest request) {
		System.out.println("user");

		User user = userService.getByusername(name);
		if (user != null && user.getPassword().equals(word)) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user.getUserName());
			System.out.print(true);
			return "redirect:/suc";
		} else {
			System.out.print(false);
			return "redirect:/fail";
		}
	}

	/**
	 * 处理 GET 请求，这里留空，可根据需求添加逻辑
	 * @return 视图名
	 */
	@GetMapping("/login")
	public String doGet() {
		return "loginPage"; // 可根据实际情况返回登录页面视图名
	}
}