package love.linyi.controller.loginandout;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/logout")
public class Logout {
    @PostMapping("")
    public String logout(HttpSession session) {

        session.invalidate();
        return "logout";
    }

}
