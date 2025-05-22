package love.linyi.Servlet;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import love.linyi.controller.Code;
import love.linyi.service.OutputFile;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebServlet("/upload")
public class UpLoadHtml extends HttpServlet {
    OutputFile outputFile;
    @Override
    public void init() throws ServletException {
        super.init();
        // 从 Spring 容器中获取 OutputFile 实例
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        outputFile = ctx.getBean(OutputFile.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("user")==null){
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write("请登录");
            response.sendRedirect(Code.host+"main.jsp");
            return;
        }
        // 检查请求是否为文件上传请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.getWriter().println("错误：请求不是文件上传请求");
            return;
        }
        outputFile.outputFile(request,response);

        // 转发到消息页面
        System.out.println(Code.host+"siwei/"+outputFile.getFilename());
        response.getWriter().write("File uploaded successfully: " + Code.host+"siwei/"+outputFile.getFilename());
    }
}