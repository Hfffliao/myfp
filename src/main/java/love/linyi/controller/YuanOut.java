package love.linyi.controller;

import love.linyi.domin.ShiJian;
import love.linyi.service.ShiJianService;
import love.linyi.service.TransWithShijian;
import love.linyi.service.impl.TransWithShijianImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/yuan1")
public class YuanOut {
    // 依赖注入 ShiJianService
    @Autowired
    private ShiJianService shiJianService;
    // 依赖注入 TransWithShijian
    @Autowired
    private TransWithShijian transWithShijian;

    @GetMapping
    public ModelAndView doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 检查用户是否登录
        if (request.getSession().getAttribute("user") == null) {
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write("请登录");
            response.sendRedirect(request.getContextPath()  + "/main.html");
            return null;
        }
        // 设置跨域访问头
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 设置响应内容类型
        response.setContentType("text/html; charset=UTF-8");

        String start = request.getParameter("start");
        String date = request.getParameter("date");
        String stop = request.getParameter("stop");

        ModelAndView modelAndView = new ModelAndView();
        if (stop != null && date != null && start != null) {
            String[] ST1 = transWithShijian.tran(start, date, stop);
            String sq1 = ST1[0];
            String sq2 = ST1[1];

            System.out.println(sq1 + sq2);
            List<ShiJian> ST = shiJianService.getArae(sq2, sq1);
            // 将数据存入模型
            modelAndView.addObject("dataList", ST);
            // 设置视图名称
            modelAndView.setViewName(request.getContextPath() +"/pages/cejv.html");
        }
        return modelAndView;
    }

    // 这里可以根据实际需求实现 POST 请求处理逻辑
    // @PostMapping
    // public ModelAndView doPost(HttpServletRequest request, HttpServletResponse response) {
    //     // TODO: 实现 POST 请求处理逻辑
    // }
}