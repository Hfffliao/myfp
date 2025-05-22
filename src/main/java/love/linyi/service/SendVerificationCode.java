package love.linyi.service;

public interface SendVerificationCode {
    void sendVerificationCode(String to ,String subject,String text);
}
