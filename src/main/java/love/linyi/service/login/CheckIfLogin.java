package love.linyi.service.login;

import javax.servlet.http.HttpSession;

public interface CheckIfLogin {
    boolean checkIfLogin(HttpSession session);
}
