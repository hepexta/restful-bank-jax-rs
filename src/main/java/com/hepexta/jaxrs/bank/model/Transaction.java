package com.hepexta.jaxrs.bank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hepexta.jaxrs.bank.model.serialize.LocalDateDeserializer;
import com.hepexta.jaxrs.bank.model.serialize.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonFormat
public class Transaction {

    @JsonProperty
    private String id;
    @JsonProperty(required = true)
    private String sourceAccountId;
    @JsonProperty(required = true)
    private String destAccountId;
    @JsonProperty(required = true)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate operDate;
    @JsonProperty(required = true)
    private BigDecimal amount;
    @JsonProperty(required = true)
    private String comment;

}
