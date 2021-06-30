package br.com.rchlo.cards.infra.email;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    private final MailSender mailSender;

    public EmailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String to, String subject, String body) {
        var message = new SimpleMailMessage();
        message.setFrom("noreply@rchlo.com.br");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);   // para verificar o email enviado acesse: https://www.smtpbucket.com/emails.
                                    // Coloque noreply@rchlo.com.br em Sender e o email do cliente no Recipient.
    }
}
