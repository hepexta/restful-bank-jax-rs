package com.hepexta.jaxrs.bank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonFormat
@ToString
public class Account {

    @JsonProperty
    private String id;
    @JsonProperty
    private String number;
    @JsonProperty(required = true)
    private Client client;
    @JsonProperty(required = true)
    private BigDecimal balance;

    public Account(Client client, BigDecimal balance) {
        this.client = client;
        this.balance = balance;
    }
}
