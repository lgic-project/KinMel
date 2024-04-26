package com.lagrandee.kinMel.helper.emailhelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;
    public void sendOtpInMail(String email,String otp) throws MessagingException {
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify OTP- Team kinMel");
        mimeMessageHelper.setText("""
                <div>
                  <a href="http://localhost:8080/verify-account?email=%s&otp=%s" target="_blank">Click link to verify or code</a>
                  </br>
                  <h1>%s</h1>
                """.formatted(email,otp,otp),true);
        javaMailSender.send(mimeMessage);
    }
}
