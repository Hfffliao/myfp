package love.linyi.Servlet;

import love.linyi.domin.User;
import love.linyi.service.SendVerificationCode;
import love.linyi.service.UserService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Register 类用于处理用户注册请求，继承自 HttpServlet。
 * 该类负责生成验证码、验证用户输入的验证码以及处理用户注册逻辑。
 */
@WebServlet("/register")
public class Register extends HttpServlet{
	/****/
	// 用于发送验证码的服务类实例
	private SendVerificationCode sendVerificationCode;
	// 用于处理用户相关业务逻辑的服务类实例
	private UserService userService;

	/**
	 * 初始化 Servlet 时调用，从 Spring 容器中获取所需的服务实例。
	 */
	@Override
	public void init() throws ServletException {
		super.init();
		// 从 Spring 容器中获取 OutputFile 实例
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		// 从 Spring 容器中获取 SendVerificationCode 实例
		sendVerificationCode = ctx.getBean(SendVerificationCode.class);
		// 从 Spring 容器中获取 UserService 实例
		userService = ctx.getBean(UserService.class);
	}

	/**
	 * 处理 HTTP GET 请求，负责用户注册流程，包括验证码生成和验证。
	 * @param req HttpServletRequest 对象，包含客户端请求信息
	 * @param res HttpServletResponse 对象，用于向客户端发送响应
	 */
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		// 设置响应内容类型为 HTML，字符编码为 UTF-8
		res.setContentType("text/html;charset=utf-8");
		// 获取用于向客户端发送文本的 PrintWriter 对象
		PrintWriter pw = res.getWriter();
		// 从请求参数中获取用户名
		String name = req.getParameter("username");
		// 从请求参数中获取密码
		String word = req.getParameter("password");
		// 从请求参数中获取用户输入的验证码
		String verification = req.getParameter("verification");
		// 打印获取到的用户名、密码和验证码
		System.out.println(name);
		System.out.println(word);
		System.out.println(verification);

		// 如果用户未输入验证码，则生成一个新的验证码
		if (verification == null) {
			if (name==null){
				pw.println("请输入用户名");
				System.out.println("请输入用户名");
				return;
			}
			User user=userService.getByusername(name);
			if(user!=null){
				pw.println("用户名已存在");
				System.out.println("用户名已存在");
				return;
			}
			// 初始化验证码为空字符串
			verification = "";
			// 随机数的最小值
			int min = 0;
			// 随机数的最大值（左闭右开区间）
			int max = 10; // 因为范围是左闭右开，所以这里设为 11
			// 循环 6 次，生成 6 位验证码
			for (int i = 0; i < 6; i++) {
				// 生成 0 到 9 之间的随机整数
				int randomInt = (int) (Math.random() * (max - min)) + min;
				// 将随机数添加到验证码字符串中
				verification = verification +randomInt;
			}
			// 打印生成的验证码
			System.out.println("生成验证码:"+verification);
			// 调用服务类发送验证码给用户
			try {
				sendVerificationCode.sendVerificationCode(name, "霖依", "感谢使用霖依，您的验证码是：" + verification);
				// 将生成的验证码存入请求属性，供后续页面使用

			} catch (Exception e) {
				pw.println("验证码发送失败");
			}
			req.setAttribute("myverification", verification);
			req.setAttribute("username", name);
			// 转发请求到注册页面
			req.getRequestDispatcher("/pages/register.jsp").forward(req, res);
		} else {
			// 如果用户输入了验证码，验证验证码是否正确
			if (verification.equals(req.getParameter("myverification"))) {
				// 打印验证码正确信息到客户端和控制台
				pw.println("验证码正确");
				System.out.println("验证码正确");
				// 验证两次输入的密码是否一致
				if (word.equals(req.getParameter("repassword"))) {
					// 打印密码两次一致信息到客户端
					pw.println("密码两次一致");
					// 调用服务类保存用户信息
					System.out.println("baoc:"+name+word);
					userService.save(new User(name, word));
					// 打印注册成功信息到客户端
					pw.print("注册成功");
					// 打印注册成功信息到控制台
					System.out.println("注册成功");
					// 打印跳转登录页面信息到客户端
					pw.print("跳转登录页面");
					// 转发请求到注册成功页面
					req.getRequestDispatcher("/pages/registersuc.jsp").forward(req, res);
					// 关闭 PrintWriter 对象
					pw.close();
				}
			}
		}
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {
		doGet(req,res);
	}

}