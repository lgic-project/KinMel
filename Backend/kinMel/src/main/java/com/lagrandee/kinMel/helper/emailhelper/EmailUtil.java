package com.lagrandee.kinMel.helper.emailhelper;

import com.lagrandee.kinMel.exception.UnableToSendMailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;
    private final ThreadPoolTaskExecutor taskExecutor;

    @Async
    public void sendOtpInMail(String email, String otp) throws MessagingException {
        taskExecutor.execute(() -> {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
                mimeMessageHelper.setTo(email);
                mimeMessageHelper.setSubject("Verify OTP- Team kinMel");

                String emailBody = """
                    <div>
                      <a href="http://localhost:8080/verify-account?email=%s&otp=%s" target="_blank">Click link to verify or code</a>
                      </br>
                      <h1>%s</h1>
                    </div>
                    """.formatted(email, otp, otp);

                mimeMessageHelper.setText(emailBody, true);
                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
               throw new UnableToSendMailException("Cannot send mail.Please try again");
            }
        });
    }
}