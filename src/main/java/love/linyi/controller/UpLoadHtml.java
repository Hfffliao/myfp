package love.linyi.controller;

import love.linyi.service.OutputFile;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UpLoadHtml {

    // 使用依赖注入获取 OutputFile 实例
    @Autowired
    private OutputFile outputFile;

    /**
     * 处理文件上传的 POST 请求
     * @param request HttpServletRequest 对象
     * @param response HttpServletResponse 对象
     */
    @PostMapping("/upload")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("user") == null) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write("请登录");
            response.sendRedirect(Code.host + "main.jsp");
            return;
        }
        // 检查请求是否为文件上传请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.getWriter().println("错误：请求不是文件上传请求");
            return;
        }
        outputFile.outputFile(request, response);

        // 输出日志并返回成功信息
        System.out.println(Code.host + "siwei/" + outputFile.getFilename());
        response.getWriter().write("File uploaded successfully: " + Code.host + "siwei/" + outputFile.getFilename());
    }
}