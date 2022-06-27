package com.bindord.eureka.keycloak.util;

public class Enums {

    public enum ResponseCode {
        REGISTRO(-1),
        ACTUALIZACION(-2),
        ELIMINACION(-3),
        GENERIC_SUCCESS(-4),
        EX_VALIDATION_FAILED(-5),
        EMPTY_RESPONSE(-6),
        SESSION_VALUE_NOT_FOUND(-7),
        SUCCESS_QUERY(-8),
        EX_GENERIC(-9),
        EX_NULL_POINTER(-10),
        EX_JACKSON_INVALID_FORMAT(-11),
        NOT_FOUND_MATCHES(-12),
        EX_NUMBER_FORMAT(-99),
        EX_MAX_SIZE_MULTIPART(-100),
        EX_MAX_UPLOAD_SIZE(-101),
        EX_ARRAY_INDEX_OUT(-102),
        EX_SQL_EXCEPTION(-103),
        EX_SQL_GRAMMAR_EXCEPTION(-104),
        ILLEGAL_ARGUMENT_EXCEPTION(-105),
        VF_USUARIO_REPETIDO(-150);

        final int code;

        ResponseCode(int code) {
            this.code = code;
        }

        public String get() {
            return String.valueOf(code);
        }
    }

    public enum Mail {
        INIT_CHANGE_PASSWORD(1),
        FINAL_CHANGE_PASSWORD(2);

        final int id;

        Mail(int id) {
            this.id = id;
        }

        public int get() {
            return id;
        }
    }

    public enum Error {
        STATUS_SERVICE("No puede activar la asociación mientras que el servicio se encuentre inactivo"),
        SERVICE_TYPE_NOT_FOUND("El tipo de servicio asignado no existe"),
        ARCHIVO_EXCEDE_MAX_PERMITIDO("El archivo que ha intentado subir excede al límite permitido, por favor suba un archivo menor o igual a %s"),
        UPLOAD_FILE_FAILED("La subida del archivo ha fallado"),
        SERVICE_DETAILS_NOT_CONFIGURED("Los detalles del servicio no han sido registrados. Configurar!"),
        BACKEND_CODE_MISMATCH("El código backend no ha sido encontrado!"),
        SOAP_ENTEL_RESPONSE_NOT_FOUND("No se ha recibido respuesta alguno por parte del servicio!"),
        DISTRIBUTOR_DETAILS_NOT_CONFIGURED("Los detalles del distribuidor no han sido registrados. Configurar!"),
        SOMETHING_WAS_WRONG("Algo ha salido mal. Inténtelo más tarde!"),
        SAME_SELLER_TO_UPDATE("El nuevo vendedor ingresado es el mismo que el actual. Ingresar otro doc. vendedor!");

        final String msg;

        Error(String msg) {
            this.msg = msg;
        }

        public String get() {
            return msg;
        }
    }

    public enum Msg {
        SUCCESS_OPERATION("Transacción exitosa"),
        SUBIDA_EXITOSA("El o los archivos han sido subidos correctamente"),
        VALIDACION_FALLIDA("La validación ha fallado"),
        USER_DOESNT_EXISTS("El usuario que ha ingresado no existe."),
        INACTIVE_USER("El usuario que ha ingresado se encuentra inactivo."),
        ENLACE_CADUCADO("El enlace ha caducado"),
        CODIGO_VERIFICACION_ERRONEO("El código de verificación remitido no coincide con el enviado"),
        ENLACE_RECUPERACION_PASS_UTILIZADO("El enlace ha ya sido utilizado"),
        RESOURCE_NOT_FOUND("No se ha encontrado el recurso solicitado"),
        DEPOSIT_ALREADY_APPROVED("El depósito ya ha sido %s con anterioridad."),
        DEPOSIT_IS_NOT_OLDEST("El vendedor tiene depósito(s) más antiguo(s) por aprobar. Por favor apruebe los depósitos en un orden según la antiguedad del depósito"),
        RECHARGE_INVALID_AMOUNT("Monto inválido de recarga, este debe ser mayor o igual a S/. 20 y el monto debe ser múltiplo de 10"),
        SELLER_PRIVELEGES_DISABLED("Actualmente su cuenta no puede realizar ventas. Entre las posibles causas pueden estar: \n - No haber depósitado el monto de lo anteriormente vendido \n - Aún no se han validado sus depósitos \n"),
        SALE_AMOUNT_EXCEEDED("Su saldo actual es de S/. %s Por favor ingrese un monto menor o igual a este"),
        SALE_AMOUNT_EXCEEDED_REC_BAG("Basado en la comisión del PDV(%s%%) y en su saldo actual S/. %s, usted puede vender un monto máximo de S/. %s ya que incluyendo la comisión el PDV recibiría S/. %s"),
        SELLER_SALE_BAG_NOT_FOUND("Ninguna bolsa de crédito coincide con los datos enviados"),
        INVALID_USERNAME("El nombre de usuario debe ser numérico de entre 8 y 11 caracteres, de preferencia el mismo que el DNI, RUC o CE"),
        INVALID_DOCUMENT("El documento debe ser numérico de entre 8 y 11 caracteres"),
        INVALID_PASSWORD("La contraseña debe ser númerica de un tamaño de 6 dígitos"),
        INVALID_UPDATE_EMAIL("El valor del nuevo correo es el mismo que el actual"),
        INVALID_INTERRUPTION_TIME("El tiempo de interrupción enviado no es válido"),
        POS_CRED_LINE_NOT_FOUND("Aún no se le ha asignado una línea de crédito al PDV. Comuníqueselo a su distribuidor"),
        POS_CRED_LINE_NOT_FOUND_AT_BAG_LVL("Aún no se le ha asignado una LC al PDV en almenos un servicio del tipo de bolsa ingresado"),
        RESOURCE_NOT_FOUND_ALT("No se ha encontrado el recurso solicitado: %s"),
        SERVICE_PRIVILEGE_DISABLED("Usted no cuenta con permisos para interactuar con este servicio");

        final String msg;

        Msg(String msg) {
            this.msg = msg;
        }

        public String get() {
            return msg;
        }
    }

    public enum FileExt {
        JPEG(".jpg"),
        PDF(".pdf"),
        WEBM(".webm");

        final String id;

        FileExt(String id) {
            this.id = id;
        }

        public String get() {
            return id;
        }
    }

    public enum MainUSSDOps {
        RECARGA(1),
        RECAUDO(2),
        ULTIMAS_VENTAS(3),
        DATOS_COMERCIO(4),
        ACCESO_VENDEDOR(5),
        CAMBIO_PIN(6),
        REGISTRAR_DEPOSITO(7);

        final int val;

        MainUSSDOps(int val) {
            this.val = val;
        }

        public int get() {
            return val;
        }
    }

    public enum MainUSSDDCOps {
        CONSULTAR_SALDO(1),
        TOTAL_VENTAS_DIA(2),
        ULTIMOS_ABONOS(3),
        GANANCIA(4);

        final int val;

        MainUSSDDCOps(int val) {
            this.val = val;
        }

        public int get() {
            return this.val;
        }
    }

    public enum ComissionType {
        PORCENTAJE(1),
        FIJA(2);

        final int val;

        ComissionType(int val) {
            this.val = val;
        }

        public int get() {
            return this.val;
        }
    }
}
