package com.bindord.eureka.keycloak.service.email.impl;

import com.bindord.eureka.keycloak.domain.dto.MailDto;
import com.bindord.eureka.keycloak.generic.EmailGeneric;
import com.bindord.eureka.keycloak.service.email.EmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl extends EmailGeneric implements EmailService {

    @Value("${spring.profiles.active}")
    private String profile;

    @Value("${spring.mail.username}")
    private String emitterMail;

    private JavaMailSender emailSender;

    public static final Logger LOGGER = LogManager.getLogger(EmailServiceImpl.class);

    @Autowired
    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void enviarCorreoInformativo(String asunto, String receptor, String contenido) {
    }

    @Override
    public void enviarCorreoInformativoVariosBbc(String asunto, String receptores, String contenido) {
        MimeMessagePreparator preparator;
        try {
            if (profile.equals("production")) {
                preparator = mimeMessagePreparatorForRecepientsBbc(asunto, receptores, contenido);
                emailSender.send(preparator);
                return;
            }
            //Block development/qa
            if (profile.equals("gcp-dev")) {
                String receptorDev = "peterpaul.0194@gmail.com";
                preparator = mimeMessagePreparatorForRecepientsBbc(asunto, receptorDev, contenido);
                emailSender.send(preparator);
            }

            if (profile.equals("development")) {
                Integer ixUrl = contenido.indexOf("href=");
                String url = ixUrl == -1 ? "" : contenido.substring(contenido.indexOf("href=") + 6).split("'")[0];
//                bandejaTemporalRepository.save(new TemporalInbox(asunto, contenido, url));
            }
        } catch (MailException ex) {
            //Importante el log.error ya que este dispara el envío del error al correo configurado en el SMTP del log4j2.xml
            LOGGER.error(ex.getMessage());
        }
    }

    @Override
    public void enviarCorreoInformativoConUnicoCc(String asunto, String receptorPrincipal, String ccReceptor, String contenido) {
        MimeMessagePreparator preparator;
        try {
            //if(profile.equals("production")){
            preparator = mimeMessagePreparatorForRecepientAndOnlyOneCc(asunto, receptorPrincipal, ccReceptor, contenido);
            emailSender.send(preparator);
            return;
            //}

            /*//Block development/qa
            if(profile.equals("qa-azure")){
                String receptorDev = "yoselin.rodriguez@itsight.pe";
                preparator = mimeMessagePreparatorForRecepientsBbc(asunto, receptorDev, contenido);
                emailSender.send(preparator);
            }

            if(profile.equals("development")){
                Integer ixUrl = contenido.indexOf("href=");
                String url = ixUrl == -1 ? "" : contenido.substring(contenido.indexOf("href=")+6).split("'")[0];
                bandejaTemporalRepository.save(new TemporalInbox(asunto, contenido, url));
            }*/
        } catch (MailException ex) {
            //Importante el log.error ya que este dispara el envío del error al correo configurado en el SMTP del log4j2.xml
            LOGGER.error(ex.getMessage());
        }
    }

    @Override
    public void sendMail(MailDto mail) {
        MimeMessagePreparator preparator;
        try {
            //if(profile.equals("production")){
            preparator = mimeMessagePreparator(mail.getSubject(), mail.getBody(), mail.getReceivers());
            emailSender.send(preparator);
            //}
        } catch (MailException ex) {
            //Importante el log.error ya que este dispara el envío del error al correo configurado en el SMTP del log4j2.xml
            LOGGER.error(ex.getMessage());
        }
    }
}
