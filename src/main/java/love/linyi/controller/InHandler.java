package love.linyi.controller;

import love.linyi.domin.User;
import love.linyi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

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
	@GetMapping("/tencent4135514882438919469.txt")
	public void doGet(HttpServletResponse response) throws IOException {
		// 指定 txt 文件的路径
		String filePath = "/siwei/tencent4135514882438919469.txt";
		File file = new File(filePath);

		// 设置响应头
		response.setContentType("text/plain");
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "attachment; filename=\"tencent4135514882438919469.txt\"");

		// 读取文件内容并写入响应流
		try (FileInputStream fis = new FileInputStream(file);
			 OutputStream os = response.getOutputStream()) {
			byte[] buffer = new byte[1024*2];
			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.flush();
		}  catch (IOException e) {
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.getWriter().write("Error reading file");
		System.err.println("Error reading file: " + e.getMessage());
		e.printStackTrace();
		}
	}
}