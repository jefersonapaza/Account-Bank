package com.bootcamp.bank.utils;

public class Constants {

    public static final double COMMISSION_CORRIENTE = 30;
    public static final int MOVEMENT_LIMIT_AHORRO = 10;
    public static final int MOVEMENT_LIMIT_PLAZO_FIJO = 1;
    /* TIPO DE CLIENTE */
    public static final String PERSONAL = "PERSONAL";
    public static final String BUSINESS = "EMPRESARIAL";


    /* CUENTAS BANCARIAS - PASIVOS */
    public static final String SAVING_ACCOUNT = "AHORRO";
    public static final String FIXEDTERM_ACCOUNT = "PLAZO-FIJO";
    public static final String CHECKING_ACCOUNT = "CUENTA-CORRIENTE";

    /*CUENTA DE CREDITOS - ACTIVOS */
    public static final String BUSINESS_ACCOUNT = "EMPRESARIAL";
    public static final String CREDIT_CARD = "TARJETA-CREDITO";
    public static final String PERSONAL_ACCOUNT = "PERSONAL";


    /*SECUENCIAS */
    public static final String ACCOUNT_SEQUENCE = "account_sequence";


    /* MOVIMIENTOS */
    public static final String MOV_ACCOUNT = "MOV_ACCOUNT";

    public static final String MOV_CREDIT = "MOV_CREDIT" ;

    public static final String MOV_CUSTOMER = "MOV_CUSTOMER";

    public static final String MOV_PAY = "MOV_PAY";

    public static final String MOV_DEPOSIT_MONEY = "DEPOSIT_MONEY";

    public static final String MOV_WITHDRAW_MONEY = "WITHDRAW_MONEY";


    /*COSTO DE TRANSACCION*/
    public static final Float COMMISION_TRANSACTION = 0.19F ;
    public static final Integer SAVING_MAX_TRANSACTION = 5;
    public static final Integer FIXEDTERM_MAX_TRANSACTION = 5;
    public static final Integer CHECKING_MAX_TRANSACTION = 5;
    public static final Integer BUSINESS_MAX_TRANSACTION = 5;
    public static final Integer CREDIT_CARD_MAX_TRANSACTION = 5;
    public static final Integer PERSONAL_MAX_TRANSACTION = 5;


}

