package love.linyi.Servlet;


import love.linyi.controller.Code;
import love.linyi.domin.ShiJian;
import love.linyi.service.ShiJianService;
import love.linyi.service.TransWithShijian;
import love.linyi.service.impl.TransWithShijianImpl;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/yuan1")
public class YuanOut extends HttpServlet {
	private static final long serialVersionUID = 1L;
    ShiJianService shiJianService;
    TransWithShijian transWithShijian=new TransWithShijianImpl();
    @Override
    public void init() throws ServletException {
        super.init();
        // 从 Spring 容器中获取 OutputFile 实例
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        shiJianService = ctx.getBean(ShiJianService.class);
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // �������
        if (request.getSession().getAttribute("user")==null){
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write("请登录");
            response.sendRedirect(Code.host+"main.jsp");
            return;
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        // ��ֹ��������
        response.setContentType("text/html; charset=UTF-8");
        String start = request.getParameter("start");
        String date = request.getParameter("date");
        String  stop = request.getParameter("stop");
        if (stop!=null&&date!=null&&start!=null){
           // System.out.println("yuanout");
            //System.out.println(start);
            String[] ST1;
            ST1=transWithShijian.tran(start,date,stop);
            String sq1=ST1[0];
            String sq2=ST1[1];
            //String[][] ST=new String[2][100];

            // ���ؾ�̬�ַ���
            System.out.println(sq1+sq2);
            List<ShiJian> ST=shiJianService.getArae(sq2,sq1);
            request.setAttribute("dataList",ST); // 将数据存入请求作用域
            request.getRequestDispatcher("/pages/cejv.jsp").forward(request, response);
        }
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
