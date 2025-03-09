package com.helio.adapter;

import com.helio.config.TwilioConfig;
import com.helio.data.request.EmailRequest;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class MessageAdapter {

    private final TwilioConfig twilioConfig;

    private final JavaMailSender mailSender;

    @Value(value = "${spring.mail.username}")
    private String senderEmailAddress;

    public MessageAdapter(TwilioConfig twilioConfig, JavaMailSender mailSender) {
        this.twilioConfig = twilioConfig;
        this.mailSender = mailSender;
    }

    public String sendTextMessage(Long receiverMobileNumber, String body) {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        Message message = Message.creator(new PhoneNumber("+91".concat(String.valueOf(receiverMobileNumber))),
                new PhoneNumber(twilioConfig.getSenderMobileNumber()), body).create();
        return message.getSid();
    }

    public String sendEmail(EmailRequest emailRequest) {
        try {
            String emailInviteId = "EM".concat(UUID.randomUUID().toString()).replaceAll("-", "");
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmailAddress);
            mailMessage.setTo(emailRequest.getRecipient());
            mailMessage.setText(emailRequest.getMsgBody());
            mailMessage.setSubject(emailRequest.getSubject());
            mailSender.send(mailMessage);
            return emailInviteId;
        } catch (Exception exception) {
            log.info("Something went wrong while sending email to - {}",
                    emailRequest.getRecipient());
        }
        return null;
    }
}


