package love.linyi.service.domain.auth;

public interface SendVerificationCode {
    void sendVerificationCode(String to ,String subject,String text);
}
