package love.linyi.service.domain.auth;

import jakarta.servlet.http.HttpSession;

public interface CheckIfLogin {
    boolean checkIfLogin(HttpSession session);
}
