package com.hemofarm.g_track.ui.login.mail;

import android.widget.Toast;

import com.hemofarm.g_track.ui.login.LoginActivity;

import java.security.Security;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

public class GMailSender extends jakarta.mail.Authenticator {

    public Session session;


    public GMailSender() {
        String d_host = "smtp.gmail.com";
        String d_port = "587";
        Properties props = new Properties();
        props.put("mail.smtp.user", LoginActivity.sender);
        props.put("mail.smtp.host", d_host);
        props.put("mail.smtp.port", d_port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true"); // obavezno TLS
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");


        props.put("mail.smtp.user", LoginActivity.sender); // User name
        props.put("mail.smtp.password", LoginActivity.password); // password

        session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(LoginActivity.sender, LoginActivity.password);
            }
        });

        session.setDebug(true); // prikazuje detaljan log konekcije
    }




    public synchronized void sendMail(String subject, String body, String sender, String recipients) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain")));

            if (recipients.contains(",")) {
                message.setRecipients(jakarta.mail.Message.RecipientType.TO, InternetAddress.parse(recipients));
            } else {
                message.setRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(recipients));
            }

            Transport.send(message);

           System.out.println("Uspešno poslata e-poruka!");
        } catch (Exception e) {

            System.out.println("Greška prilikom slanja mejla: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void sendMailWithAttachment(String subject, String body, String sender, String recipients, byte[] csvData) {
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setSubject(subject);

            // Tekstualni deo
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);

            // CSV deo
            MimeBodyPart attachmentPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(csvData, "text/csv");
            attachmentPart.setDataHandler(new DataHandler(source));
            attachmentPart.setFileName("logovi.csv");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            if (recipients.contains(",")) {
                message.setRecipients(jakarta.mail.Message.RecipientType.TO, InternetAddress.parse(recipients));
            } else {
                message.setRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(recipients));
            }

            Transport.send(message);
            System.out.println("Uspešno poslata e-poruka sa CSV prilogom!");
        } catch (Exception e) {
            System.out.println("Greška (sendMailWithAttachment): " + e.getMessage());
            e.printStackTrace();
        }
    }

}


