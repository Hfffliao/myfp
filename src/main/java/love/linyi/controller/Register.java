package love.linyi.controller;
import love.linyi.domin.User;
import love.linyi.service.SendVerificationCode;
import love.linyi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;
import org.springframework.web.servlet.function.ServerResponse;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Register 类用于处理用户注册请求，继承自 HttpServlet。
 * 该类负责生成验证码、验证用户输入的验证码以及处理用户注册逻辑。
 */
@RestController
@ResponseBody
public class Register{
	/****/
	// 用于发送验证码的服务类实例
	@Autowired
	private SendVerificationCode sendVerificationCode;
	// 用于处理用户相关业务逻辑的服务类实例
	@Autowired
	private UserService userService;

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
		String verification ;
		// 打印获取到的用户名、密码和验证码
		System.out.println(name);
		if (name == null ||name.equals("")||name.equals("null")) {
			pw.print("email error");
			System.out.println("email error");
			return;
		}
		User user = userService.getByusername(name);
		if (user != null) {
			pw.print("user exist");
			System.out.println("user exist");
			return;
		}
		// 初始化验证码
		verification = getverification();
		// 打印生成的验证码
		//System.out.println("生成验证码:" + verification);

		try {
			// 调用服务类发送验证码给用户
			sendVerificationCode.sendVerificationCode(name, "霖依", "感谢使用霖依，您的验证码是：" + verification);
			// 将生成的验证码存入请求属性，供后续页面使用
			storeCaptcha(verification, request.getSession());
			request.getSession().setAttribute("username", name);
		    pw.print("send success");
		} catch (Exception e) {
			pw.print("send failed");
		}


	}
	@PostMapping("/register")
	public ResponseEntity<Map<String,String>> doPost(HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
		Map<String,String> result = new HashMap<>();
		Object count =request.getSession().getAttribute("count");
		//count用于防止别人反复输入验证码
		if (count==null){
			request.getSession().setAttribute("count",0);
			count=0;
		}
		count=(int)count+1;
		request.getSession().setAttribute("count",count);
		System.out.println(count);
		if (((int)count)>5){
			request.getSession().removeAttribute("verification");
			request.getSession().removeAttribute("verificationExpire");
			count=0;
			request.getSession().setAttribute("count",count);
			result.put("status","验证码输入错误次数过多");
			return ResponseEntity.ok(result);
		}
		String name =(String) request.getSession().getAttribute("username");
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
				result.put("status","success");
				return ResponseEntity.ok(result);				// 关闭 PrintWriter 对象

			}
			else {
				System.out.println("two word error");
				result.put("status","两次密码输入不一致");
				return ResponseEntity.ok(result);
			}
		}else {
			// 打印验证码错误信息到控制台
			System.out.println("verification error");
			// 验证失败，返回错误信息
			result.put("status","验证码输入错误");
			return ResponseEntity.ok(result);
		}
	}



}