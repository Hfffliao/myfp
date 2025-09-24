package love.linyi.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
public class  SessionUserGeter {

    @GetMapping("/getSessionUser")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        if(user==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        else{
            response.put("user", user);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}