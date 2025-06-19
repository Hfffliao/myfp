package love.linyi.controller;

import love.linyi.exception.SystemException;
import love.linyi.service.OutputFile;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
       // 检查用户是否登录
        if (request.getSession().getAttribute("user") == null) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write("please login");
            System.out.println("please login");
            return;
        }
        // 检查请求是否为文件上传请求
        if (!ServletFileUpload.isMultipartContent(request)) {
            response.getWriter().println("request is not file upload request");
            System.out.println("request is not file upload request");
            return;
        }
        // 处理文件上传逻辑
        try {// 检查是否已经在上传文件
            boolean peopleValue = request.getSession().getAttribute("people") == null ? false : (Boolean) request.getSession().getAttribute("people");

            if (peopleValue == false) {
                outputFile.outputFile(request, response);
                System.out.println("dao upload");
            }
            response.getWriter().println("upload success");
            System.out.println("upload success");


        } catch (Exception e) {
            response.getWriter().println("upload error");
            System.out.println("upload error");
            throw new SystemException(233, "upload error", e);
        }


        // 输出日志并返回成功信息
//System.out.println(Code.host + "siwei/" + outputFile.getFilename());
        // 转发请求到注册页面
    }
    @GetMapping("/upload-progress")
    public void getUploadProgress(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 这里需要实现获取上传进度的逻辑
        // UploadProgressListener 类中保存了上传进度信息
        Object progress = request.getSession().getAttribute("uploadProgress"); // 假设存在 getProgress 方法获取进度
        System.out.println("progress = " + progress);
           // response.setContentType("text/plain");
        if (progress == null) {
            progress = 0;
        }
            response.getWriter().write(String.valueOf(progress));

    }
}