package com.park.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(
            JavaMailSender javaMailSender
    ) {
        this.javaMailSender = javaMailSender;
    }

    public void enviarEmail(String destinatario, String assunto, String mensagem) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(mensagem);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Lidar com exceções de envio de e-mail, se necessário
            e.printStackTrace();
        }
    }
}
