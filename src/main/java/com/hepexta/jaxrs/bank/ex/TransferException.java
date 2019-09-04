package com.hepexta.jaxrs.bank.ex;

public class TransferException extends RuntimeException {

    public static final String ACCOUNT_NOT_FOUND_BY_ID = "Account not found by id %s";
    public static final String CLIENT_NOT_FOUND_BY_ID = "Client not found by id %s";
    public static final String NOT_ENOUGH_MONEY_ERROR_DEPOSIT = "%s doesn't have enough money for deposit";
    public static final String NOT_ENOUGH_MONEY_ERROR_WITHDRAWAL = "%s doesn't have enough money for withdraw";
    public static final String ERROR_INSERTING_ACCOUNT = "Error inserting account";
    public static final String ERROR_INSERTING = "Error inserting";
    public static final String ERROR_INSERTING_TRANSACTION = "Error inserting transaction";

    public TransferException(String msg) {
        super(msg);
    }

    public TransferException(String message, Exception e) {
        super(message, e);
    }
}
