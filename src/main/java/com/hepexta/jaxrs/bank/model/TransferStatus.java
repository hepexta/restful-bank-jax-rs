package com.hepexta.jaxrs.bank.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransferStatus {

    NEW("New", "Transaction created at %s"),
    SUCCESS("Success", "Successfully executed at %s"),
    ERROR("Error", "%s"),
    ;

    private String status;
    private String message;
}
