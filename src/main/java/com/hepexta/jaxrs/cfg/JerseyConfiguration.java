package com.hepexta.jaxrs.cfg;

import com.hepexta.jaxrs.bank.ex.TransactionExceptionHandler;
import com.hepexta.jaxrs.bank.repository.AccountFactory;
import com.hepexta.jaxrs.bank.repository.ClientFactory;
import com.hepexta.jaxrs.bank.repository.TransferFactory;
import com.hepexta.jaxrs.bank.service.AccountService;
import com.hepexta.jaxrs.bank.service.ClientService;
import com.hepexta.jaxrs.bank.service.TransferService;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class JerseyConfiguration extends ResourceConfig {

    public JerseyConfiguration() {
        packages("com.hepexta", "com.fasterxml.jackson.jaxrs.json");
        register(JacksonFeature.class);
        register(TransactionExceptionHandler.class);
        register(new ClientService(ClientFactory.getClientRepository()));
        register(new AccountService(AccountFactory.getAccountRepository(), ClientFactory.getClientRepository()));
        register(new TransferService(AccountFactory.getAccountRepository(), TransferFactory.getLockRepository(), TransferFactory.getTransRepository()));
    }
}