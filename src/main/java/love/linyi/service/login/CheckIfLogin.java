package love.linyi.service.login;

import jakarta.servlet.http.HttpSession;

public interface CheckIfLogin {
    boolean checkIfLogin(HttpSession session);
}
