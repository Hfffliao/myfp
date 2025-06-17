package love.linyi.controller;

import love.linyi.domin.User;
import love.linyi.service.SendVerificationCode;
import love.linyi.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Register 类用于处理用户注册请求，继承自 HttpServlet。
 * 该类负责生成验证码、验证用户输入的验证码以及处理用户注册逻辑。
 */
@Controller
public class Register extends HttpServlet{
	/****/
	// 用于发送验证码的服务类实例
	@Autowired
	private SendVerificationCode sendVerificationCode;
	// 用于处理用户相关业务逻辑的服务类实例
	@Autowired
	private UserService userService;
    @Autowired
    private HttpSession httpSession;


	private String getverification(){
		int min = 0;
		// 随机数的最大值（左闭右开区间）
		int max = 10; // 因为范围是左闭右开，所以这里设为 10
		String randomInt="";
		// 循环 6 次，生成 6 位验证码
		for (int i = 0; i < 6; i++) {
			// 生成 0 到 9 之间的随机整数
			 randomInt += (int) (Math.random() * (max - min)) + min;
			// 将随机数添加到验证码字符串中
		}
		return randomInt;
	}
	private void storeCaptcha(String verification, HttpSession session) {
		session.setAttribute("verification", verification);
		session.setAttribute("verificationExpire", System.currentTimeMillis() + 5 * 60 * 1000); // 5分钟
	}
	private boolean validateCaptcha(String storedCaptcha, Long expireTime, String input) {
		// 双重检查：验证码是否存在、是否过期
		if (storedCaptcha == null || expireTime == null) {
			return false; // 验证码不存在
		}

		if (System.currentTimeMillis() > expireTime) {
			return false; // 验证码已过期
		}

		return storedCaptcha.equals(input); // 比对验证码（不区分大小写）
	}
	private void sendverificationimage(String verification, HttpServletResponse response){

	}

	@GetMapping("/register")
	public void doGet(HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
		// 设置响应内容类型为 HTML，字符编码为 UTF-8
		response.setContentType("text/html;charset=utf-8");
		// 获取用于向客户端发送文本的 PrintWriter 对象
		PrintWriter pw = response.getWriter();
		String name = request.getParameter("username");
		String word = request.getParameter("password");
		String verification ;
		// 打印获取到的用户名、密码和验证码
		System.out.println(name);
		System.out.println(word);
		if (name == null) {
			pw.println("请输入用户名（邮箱）");
			System.out.println("请输入用户名");
			return;
		}
		User user = userService.getByusername(name);
		if (user != null) {
			pw.println("用户名已存在");
			System.out.println("用户名已存在");
			return;
		}
		// 初始化验证码
		verification = getverification();
		// 打印生成的验证码
		//System.out.println("生成验证码:" + verification);
		// 调用服务类发送验证码给用户
		try {
			sendVerificationCode.sendVerificationCode(name, "霖依", "感谢使用霖依，您的验证码是：" + verification);
			// 将生成的验证码存入请求属性，供后续页面使用

		} catch (Exception e) {
			pw.println("验证码发送失败");
		}
		// 3. 生成验证码图片

		storeCaptcha(verification, request.getSession());
		request.setAttribute("username", name);
		// 转发请求到注册页面
		request.getRequestDispatcher("/pages/register.jsp").forward(request, response);

	}
	@PostMapping("/register")
	public void doPost( HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
		// 设置响应内容类型为 HTML，字符编码为 UTF-8
		response.setContentType("text/html;charset=utf-8");
		// 获取用于向客户端发送文本的 PrintWriter 对象
		PrintWriter pw = response.getWriter();
		String name = request.getParameter("username");
		String word = request.getParameter("password");
		String verification = (String) request.getSession().getAttribute("verification");
		Long verificationExpire = (Long) request.getSession().getAttribute("verificationExpire");
		/*调试
		System.out.println(name);
		System.out.println(word);
		System.out.println(verification);
		System.out.println(verificationExpire);
		System.out.println(request.getParameter("verification"));*/
		// 如果用户输入了验证码，验证验证码是否正确

		if (validateCaptcha(verification, verificationExpire, request.getParameter("verification"))) {
			// 验证成功后清除session中的验证码

			if (!word.equals("")&&word.equals(request.getParameter("repassword"))) {
				// 调用服务类保存用户信息
				System.out.println("baoc:"+name+word);
				userService.save(new User(name, word));
				// 转发请求到注册成功页面
				request.getSession().removeAttribute("verification");
				request.getSession().removeAttribute("verificationExpire");
				request.getRequestDispatcher("/pages/registersuc.jsp").forward(request, response);
				// 关闭 PrintWriter 对象
				pw.close();
			}
			else {
				System.out.println("two word error");
				pw.println("两次密码不一致或者密码为空");
			}
		}else {
			// 验证失败，返回错误信息
			pw.println("验证码错误或已过期");
			// 打印验证码错误信息到控制台
			System.out.println("verification error");
			// 关闭 PrintWriter 对象
			pw.close();
		}
	}



}