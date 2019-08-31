package com.hepexta.jaxrs.bank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@AllArgsConstructor
@Builder
@XmlRootElement
@ToString
public class Client {

    @JsonIgnore
    private String id;
    @JsonProperty(required = true)
    private String name;

    public Client(String name) {
        this.name = name;
    }
}
