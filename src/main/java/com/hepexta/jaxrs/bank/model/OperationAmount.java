package com.hepexta.jaxrs.bank.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationAmount {

    @JsonProperty(required = true)
    private String id;
    @JsonProperty(required = true)
    private BigDecimal amount;
    @JsonProperty
    private Date date;
}
