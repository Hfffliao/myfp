package love.linyi.Servlet;

import love.linyi.controller.Code;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class suc
 */
@WebServlet("/suc")
public class SucSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getSession().getAttribute("user")!=null){
			response.setContentType("text/html;charset=utf-8");

			//request.getRequestDispatcher("/maint.jsp").forward(request, response);
			response.sendRedirect(Code.host+"pages/maint.jsp");
		}
		else{
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write("请登录");
			response.sendRedirect(Code.host+"main.jsp");
            return;

		}


	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
