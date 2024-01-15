package com.bindord.eureka.keycloak.generic;

import org.springframework.mail.javamail.MimeMessagePreparator;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import java.util.Set;

public abstract class EmailGeneric {

    private static final String ADDRESS = "jaipro.admin@jaipro.pe";

    private static final String MAIL_CONTENT_TYPE = "text/html; charset=utf-8";
    private static final String PLATFORM_NAME = "Jaipro";


    public MimeMessagePreparator mimeMessagePreparator(String asunto, String contenido, Set<String> receptores) {
        String parseReceptores = String.join(",", receptores);
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setFrom(new InternetAddress(ADDRESS, PLATFORM_NAME));
            mimeMessage.setSubject(asunto);
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(parseReceptores));
            mimeMessage.setContent(contenido
                    , MAIL_CONTENT_TYPE);
        };
        return preparator;
    }

    public MimeMessagePreparator mimeMessagePreparatorForRecepientsBbc(String asunto, String receptors, String contenido) {

        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setFrom(new InternetAddress(ADDRESS, PLATFORM_NAME));
            mimeMessage.setSubject(asunto);
            mimeMessage.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(receptors));
            mimeMessage.setContent(contenido
                    , MAIL_CONTENT_TYPE);
        };
        return preparator;
    }

    public MimeMessagePreparator mimeMessagePreparatorForRecepientAndOnlyOneCc(String asunto, String receptor, String ccReceptor, String contenido) {

        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setFrom(new InternetAddress(ADDRESS, PLATFORM_NAME));
            mimeMessage.setSubject(asunto);
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(receptor));
            mimeMessage.setRecipient(Message.RecipientType.CC, new InternetAddress(ccReceptor));
            mimeMessage.setContent(contenido
                    , MAIL_CONTENT_TYPE);
        };
        return preparator;
    }
}
