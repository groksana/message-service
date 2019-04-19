package com.gromoks.aws.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;
import com.amazonaws.services.simpleemail.model.SendRawEmailResult;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class MailService {
    @Value("${aws-ses.credentials.sourceEmail}")
    private String sourceEmail;

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final S3Service s3Service;

    public MailService(AmazonSimpleEmailService amazonSimpleEmailService, S3Service s3Service) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
        this.s3Service = s3Service;
    }

    public void send() {
        Session session = Session.getDefaultInstance(new Properties());
        String email = sourceEmail;

        InputStream inputStream = s3Service.downloadFile("templates/test.html");
        String body = "";
        try {
            body = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
            logger.info("Mail Subject " + body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            mimeMessage.setSubject("Test Subj", StandardCharsets.UTF_8.displayName());
            final MimeMessageHelper message =
                    new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.displayName());
            message.setText(body, true);
            mimeMessage.setFrom(new InternetAddress(email));
            mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(email));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            mimeMessage.writeTo(outputStream);
            RawMessage rawMessage =
                    new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

            SendRawEmailRequest rawEmailRequest =
                    new SendRawEmailRequest(rawMessage);
            SendRawEmailResult sendRawEmailResult = amazonSimpleEmailService.sendRawEmail(rawEmailRequest);
            logger.info("Email has been sent to {} with result: {}", email, sendRawEmailResult);
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}
