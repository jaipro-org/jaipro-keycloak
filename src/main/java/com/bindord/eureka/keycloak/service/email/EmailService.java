package com.bindord.eureka.keycloak.service.email;

import com.bindord.eureka.keycloak.domain.dto.MailDto;

public interface EmailService {

	void enviarCorreoInformativo(String asunto, String receptor, String contenido);

	void enviarCorreoInformativoVariosBbc(String asunto, String receptores, String contenido);

	void enviarCorreoInformativoConUnicoCc(String asunto, String receptorPrincipal, String ccReceptor, String contenido);

	void sendMail(MailDto mailDto);
}
