package love.linyi.service.impl;

import love.linyi.service.SendVerificationCode;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


@Service
public class SendVerificationCodeImpl implements SendVerificationCode {
    @Override
      public void sendVerificationCode(String to, String subject, String text) {        // 配置邮件服务器属性
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.qq.com"); // 替换为你的 SMTP 服务器地址
        properties.put("mail.smtp.port", "587"); // 替换为你的 SMTP 服务器端口
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // 认证信息
        final String username = "3390351358@qq.com"; // 替换为你的邮箱地址
        final String password = "cldoaoylrkspdbbb"; // 替换为你的邮箱密码或授权码

        // 创建会话
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 创建邮件消息
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(text);

            // 发送邮件
            Transport.send(message);
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            System.out.println("邮件发送失败: " + e.getMessage());
        }
    }
}
