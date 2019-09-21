package com.hepexta.jaxrs.bank.ex;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.ws.rs.core.Response;

import static com.hepexta.jaxrs.bank.ex.ErrorMessageText.*;

@AllArgsConstructor
@Getter
public enum ErrorMessage {

    ERROR_500(500, Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()),
    ERROR_520(520, ACCOUNT_NOT_FOUND_BY_ID),
    ERROR_521(521, AMOUNT_SHOULD_BE_GREATER_THAN_ZERO),
    ERROR_522(522, CLIENT_NOT_FOUND_BY_ID),
    ERROR_523(523, NOT_ENOUGH_MONEY_ERROR_DEPOSIT),
    ERROR_524(524, NOT_ENOUGH_MONEY_ERROR_WITHDRAWAL),
    ERROR_525(525, ERROR_INSERTING_ACCOUNT),
    ERROR_526(526, ERROR_INSERTING_CLIENT),
    ERROR_527(527, ERROR_INSERTING_TRANSACTION),
    ERROR_528(528, ERROR_TRANSACTION_ACCOUNTS_ARE_EQUALS),
    ERROR_529(529, ERROR_SOURCE_ACCOUNT_NOT_FOUND),
    ERROR_530(530, ERROR_DEST_ACCOUNT_NOT_FOUND),
    ERROR_531(531, NOT_ENOUGH_MONEY_ERROR_TRANSFER),
    ERROR_532(532, ERROR_INSERTING);

    private int code;
    private String message;
}
