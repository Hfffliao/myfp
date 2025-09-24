package love.linyi.service.login.Impl;

import love.linyi.service.login.CheckIfLogin;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
@Service
public class CheckIfLoginImpl implements CheckIfLogin {
    @Override
    public boolean checkIfLogin(HttpSession session) {
        String name = (String) (session.getAttribute("user")==null? "":session.getAttribute("user"));
        //获取用户id并检查是否为空
        int id = (int) (session.getAttribute("id")==null? 0:session.getAttribute("id"));
        // 检查用户是否登录
        if (name.equals("")||id==0) {

            return false;
        }
        return true;
    }
}
