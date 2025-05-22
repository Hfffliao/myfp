package love.linyi.Servlet;
import love.linyi.domin.ShiJian;
import love.linyi.service.ShiJianService;
import love.linyi.service.impl.AcquisitionTimeImpl;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Servlet implementation class Http
 */
@WebServlet("/yuan")
public class YuanIn extends HttpServlet {
	private ShiJianService shiJianService;
	ShiJian shiJian;
	@Override
	public void init() throws ServletException {
		super.init();
		// 从 Spring 容器中获取 OutputFile 实例
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		shiJianService = ctx.getBean(ShiJianService.class);
		shiJian=new ShiJian();
	}
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int d=0;
		AcquisitionTimeImpl get=new AcquisitionTimeImpl();
		BufferedReader reader = request.getReader();
		String re=reader.readLine();
		//System.out.println("yuanin日志1："+re);
		if(re==null){re="-1";}
		//System.out.println("yuanin日志2："+re);
		shiJian.setOtime(get.getdata());
		shiJian.setDistance(Integer.parseInt(re));
		if(shiJianService.save(shiJian)){
			d=1;
		}
		reader.close();
		if(d>0){
			response.setContentType("text/plain");
			response.getWriter().write("suc");
		}

	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}
}
