package love.linyi.Servlet;

import love.linyi.controller.Code;
import love.linyi.domin.User;
import love.linyi.service.ShiJianService;
import love.linyi.service.UserService;
import love.linyi.service.impl.UserServiceImpl;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
@WebServlet("/demo3")
public class InHandler extends HttpServlet{
	/****/

	private UserService userService;
	@Override
	public void init() throws ServletException {
		super.init();
		// 从 Spring 容器中获取 OutputFile 实例
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		userService = ctx.getBean(UserService.class);
	}
	private static final long serialVersionUID = 1L;
	public void doPost(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {


		res.setContentType("text/html;charset=utf-8");
		PrintWriter pw=res.getWriter();
		String name=req.getParameter("username");
		String word=req.getParameter("password");
		System.out.println("user");

		User user= userService.getByusername(name);
		if(user!=null) {

			if(user.getPassword().equals(word)) {
				req.getSession().setAttribute("user", user);
				req.getRequestDispatcher("/suc").forward(req, res);
				System.out.print(true);
			}else {
				req.getRequestDispatcher("/fail").forward(req, res);
				System.out.print(false);
			}
		}
		System.out.print(word);
		System.out.println(user);
		pw.close();
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException {

	}

}