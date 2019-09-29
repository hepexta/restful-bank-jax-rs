package com.hepexta.jaxrs.bank.ex;

public interface ErrorMessageText {
    String ACCOUNT_NOT_FOUND_BY_ID = "Account not found by id %s";
    String AMOUNT_SHOULD_BE_GREATER_THAN_ZERO = "Amount should be greater than zero";
    String CLIENT_NOT_FOUND_BY_ID = "Client not found by id %s";
    String NOT_ENOUGH_MONEY_ERROR_DEPOSIT = "Account %s doesn't have enough money for deposit";
    String NOT_ENOUGH_MONEY_ERROR_WITHDRAWAL = "Account %s doesn't have enough money for withdraw";
    String ERROR_INSERTING_ACCOUNT = "Error inserting account";
    String ERROR_INSERTING_CLIENT = "Error inserting client";
    String ERROR_INSERTING_TRANSACTION = "Error inserting transaction";
    String ERROR_TRANSACTION_ACCOUNTS_ARE_EQUALS = "Transfer error. Source Account equals to Destination Account";
    String ERROR_SOURCE_ACCOUNT_NOT_FOUND = "Source account not found by ID %s";
    String ERROR_DEST_ACCOUNT_NOT_FOUND = "Destination account not found by ID %s";
    String NOT_ENOUGH_MONEY_ERROR_TRANSFER = "Account %s doesn't have enough money for transfer";
    String ERROR_INSERTING = "Error inserting";
    String ACCOUNT_IS_BLOCKED = "Account %s is blocked";
    String DATABASE_ERROR = "DataBase Error %s";
}
